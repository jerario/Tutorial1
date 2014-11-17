package com.example.jerario.tutorial1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.managers.ImagesDownloader;
import com.example.jerario.tutorial1.managers.ItemManager;
import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.EndlessListListener;
import com.example.jerario.tutorial1.utils.ImageListener;
import com.example.jerario.tutorial1.utils.ImageUtils;
import com.example.jerario.tutorial1.utils.ImageUtils.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;


public class VIPViewActivity extends Activity implements ImageListener{
    private final String TAG = "VIP VIEW ACTIVITY";
    private Item item;
    private VipHandler handler;
    TextView titleView;
    TextView subTitleView;
    TextView priceView;
    TextView conditionView;
    TextView stockView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_layout_vip);
        initComponents();
        if (savedInstanceState == null) {
            item = (Item) getIntent().getSerializableExtra("item");
        } else {
            item = (Item) savedInstanceState.getSerializable("item");
        }
        showView(item);
    }

    private void initComponents(){
        handler = new VipHandler();
        handler.registerListener(this);
        titleView = (TextView) findViewById(R.id.title);
        subTitleView = (TextView) findViewById(R.id.subtitle);
        priceView = (TextView) findViewById(R.id.price);
        conditionView = (TextView) findViewById(R.id.condition);
        stockView = (TextView) findViewById(R.id.available_quantity);
        imageView = (ImageView) findViewById(R.id.pic);
    }

    private void showView(Item item){
        priceView.setText("$" + Double.toString(item.getPrice()));
        titleView.setText(item.getTitle());
        stockView.setText(getString(R.string.stock) + ": " + item.getAvailable_quantity());
        if (item.getSubtitle()!=null)
            subTitleView.setText(item.getSubtitle());
        else
            subTitleView.setText("");

        if (item.getCondition()!= null)
            if (item.getCondition().equals("new"))
                conditionView.setText(getString(R.string.isNew));
            else if (item.getCondition().equals("used"))
                conditionView.setText(getString(R.string.isUsed));
            else
                conditionView.setText("");



        //To modify
        if (item.getHQpicture() == null) {
            if (item.getQuality_picturesUrl()==null || item.getQuality_picturesUrl().isEmpty()) {
                //Searching Images
                try {
                    ImagesDownloader.getInstance().getImageUrl(handler,item);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    item.setQuality_picturesUrl(null);
                }
            }

            if (!item.isDownloading()) {
                item.setDownloading(true);
                if (item.getQuality_picturesUrl() != null)
                    ImagesDownloader.getInstance().getImage(handler,getListenerId(), -1, item.getQuality_picturesUrl().get(0));
            }

        }
        else{
            imageView.setImageBitmap(item.getHQpicture());
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("item", item);

    }

    @Override
    public int getListenerId() {
        return 7007;
    }

    @Override
    public void notifyImageAvailable(int position, Object image) {
        if (item != null){
            Log.d(TAG,"Seting the HQPicture");
            item.setHQpicture((Bitmap) image);
        }
    }


    public class VipHandler extends Handler{
        private HashMap<Integer,ImageListener> imageListenerHash;

        private VipHandler() {
            imageListenerHash = new HashMap<Integer, ImageListener>();
        }

        @Override
        public void handleMessage(Message msg){
            //Do a case url or BItmap
            if (msg.what==CONST.IMAGEDOWNLOADED) {
                Log.d(TAG,"WHAT DOWNLOADED;");
                Bitmap image = (Bitmap) msg.obj;
                item.setHQpicture(image);
                imageView.setImageBitmap(image);
            }
            else if(msg.what==CONST.URLMSG){
                Log.d(TAG,"WHAT URL;");
                item = (Item) msg.obj;
                ImagesDownloader.getInstance().getImage(handler,getListenerId(), -1, item.getQuality_picturesUrl().get(0));


            }
        }

        public void registerListener(ImageListener listener){
            imageListenerHash.put(listener.getListenerId(),listener);
        }
    }
}
