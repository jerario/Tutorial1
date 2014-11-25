package com.example.jerario.tutorial1;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.managers.ImagesDownloader;
import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.ImageListener;
import com.example.jerario.tutorial1.utils.database.TrackerSqlHelper;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by jerario on 11/18/14.
 */
public class VipViewFragment extends Fragment implements ImageListener{
    public static final String TAG = VipViewFragment.class.getName();
    private Item item;
    private VipHandler handler;
    private View rootView;
    private LinkedList<Item> productList;
    TextView titleView;
    TextView subTitleView;
    TextView priceView;
    TextView conditionView;
    TextView stockView;
    ImageView imageView;
    Button tracker;
    TrackerSqlHelper tHelper;


    public static final VipViewFragment newInstance(Bundle bundle){
        VipViewFragment fragment = new VipViewFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.product_layout_vip, container, false);
        initComponents();
        Bundle bundle = getArguments();
        if (bundle == null) {
            item = (Item) getActivity().getIntent().getSerializableExtra(CONST.ITEM);
        } else {
            item = (Item) bundle.getSerializable(CONST.ITEM);
            productList = (LinkedList<Item>) bundle.getSerializable(CONST.PRODUCTLIST);
        }
        if (item != null)
            showView(item);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initComponents();
    }

    public void updateView(Item item){
        showView(item);
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
        //Tracking item
        if (isTracked()) {
            tracker.setText(R.string.untrack);
        }
        else
            tracker.setText(R.string.track);
        tracker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tracker.getText().toString().equals(getString(R.string.untrack)))
                    untrackItem();
                else
                    trackItem();
            }
        });


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

    private void initComponents(){

        handler = new VipHandler();
        handler.registerListener(this);
        titleView = (TextView) rootView.findViewById(R.id.title);
        subTitleView = (TextView) rootView.findViewById(R.id.subtitle);
        priceView = (TextView) rootView.findViewById(R.id.price);
        conditionView = (TextView) rootView.findViewById(R.id.condition);
        stockView = (TextView) rootView.findViewById(R.id.available_quantity);
        imageView = (ImageView) rootView.findViewById(R.id.pic);
        tracker = (Button) rootView.findViewById(R.id.tracker);

        tHelper = new TrackerSqlHelper(rootView.getContext());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CONST.ITEM, item);
      //  outState.putSerializable(CONST.PRODUCTLIST,productList);
        outState.putSerializable(CONST.CALLER,CONST.VIPVIEW);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getListenerId() {
        return 7007;
    }

    @Override
    public void notifyImageAvailable(int position, Object image) {
        if (item != null){
            item.setHQpicture((Bitmap) image);
        }
    }

    private boolean isTracked(){
        return tHelper.isTracked(item.getId());
    }

    private void trackItem(){
        item.setTracked(true);
        tracker.setText(R.string.untrack);
        tHelper.trackItem(item.getId(),item.getPrice());
        Log.d(TAG,"TRACK THIS ITEM + " + item.getId());
    }

    private void untrackItem(){
        item.setTracked(false);
        tracker.setText(R.string.track);
        tHelper.untrackItem(item.getId());
        Log.d(TAG,"UNTRACK THIS ITEM");

    }

    public class VipHandler extends Handler {
        private HashMap<Integer,ImageListener> imageListenerHash;

        private VipHandler() {
            imageListenerHash = new HashMap<Integer, ImageListener>();
        }

        @Override
        public void handleMessage(Message msg){
            //Do a case url or BItmap
            if (msg.what== CONST.IMAGEDOWNLOADED) {
                Bitmap image = (Bitmap) msg.obj;
                item.setHQpicture(image);
                imageView.setImageBitmap(image);
            }
            else if(msg.what==CONST.URLMSG){
                item = (Item) msg.obj;
                ImagesDownloader.getInstance().getImage(handler,getListenerId(), -1, item.getQuality_picturesUrl().get(0));


            }
        }

        public void registerListener(ImageListener listener){
            imageListenerHash.put(listener.getListenerId(),listener);
        }
    }
}
