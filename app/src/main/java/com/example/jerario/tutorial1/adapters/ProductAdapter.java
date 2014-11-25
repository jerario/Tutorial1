package com.example.jerario.tutorial1.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerario.tutorial1.R;
import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.handlers.ImagesDownloadedHandler;
import com.example.jerario.tutorial1.managers.ImagesDownloader;
import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.EndlessListListener;
import com.example.jerario.tutorial1.utils.ImageListener;

import java.util.LinkedList;

/**
 * Adapter, put the items in the List
 * Created by jerario on 11/11/14.
 */
public class ProductAdapter extends BaseAdapter implements ImageListener{
    private static final Boolean DEBUGING = false;
    private final String TAG = "ProductAdapter";
    private LinkedList<Item> products;
    private LayoutInflater lInflater;
    private View progressView;


    public ProductAdapter(Context context, LinkedList<Item> products){
        this.lInflater = LayoutInflater.from(context);
        this.products = products;
        progressView = lInflater.inflate(R.layout.product_loading,null);
        ImagesDownloadedHandler.getInstance().registerListener(this);
    }

    @Override
    public int getCount() {
        if (products == null)
            return 0;
        else
            return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
      //  Log.d("PRODUCT ADAPTER","IN");
        ContenedorView contenedor;
        if (view == null) {
            view = lInflater.inflate(R.layout.product_layout,null);
            contenedor = new ContenedorView();

         //   contenedor.pic = (ImageView) view.findViewById(R.id.pic);
            contenedor.title = (TextView) view.findViewById(R.id.title);

            View subtitleView = view.findViewById(R.id.subtitle);
            if (subtitleView != null)
                contenedor.subtitle = (TextView) subtitleView;

            View priceView = view.findViewById(R.id.price);
            if (priceView != null)
                contenedor.price= (TextView) priceView;

            View quantityView = view.findViewById(R.id.quantity);
            if (quantityView != null)
                contenedor.available_quantity= (TextView) quantityView;

            View conditionView = view.findViewById(R.id.condition);
            if (conditionView != null)
                contenedor.condition = (TextView) conditionView;

            View imageView = view.findViewById(R.id.pic);
            if (imageView != null)
                contenedor.pic = (ImageView) imageView;

            //progressBar
          /*  if (position == products.size()) {
                View progressBar = progressView.findViewById(R.id.progressBarLoading);
                view.addFoo
                contenedor.progress = (ProgressBar) progressBar;
                contenedor.progress.setVisibility(View.VISIBLE);
            }*/
            view.setTag(contenedor);
        }
        else
            contenedor = (ContenedorView) view.getTag();

        Item item = (Item) getItem(position);
        contenedor.title.setText(item.getTitle());

        //Price
        contenedor.price.setText("$ "+ Double.toString(item.getPrice()));

        //Stock
        String stock = view.getContext().getString(R.string.stock);
        String quantity = Integer.toString(item.getAvailable_quantity());

        //Quantity
        if (contenedor.available_quantity != null)
            contenedor.available_quantity.setText(stock +":"+quantity);

        //Condition
        if (contenedor.condition != null)
            contenedor.condition.setText(item.getCondition());

        //Subtitle
        if (contenedor.subtitle != null) {
            if (item.getSubtitle() != null) {
                contenedor.subtitle.setText(item.getSubtitle());
            }
            else
                contenedor.subtitle.setText(" ");
        }



        //Image
        if (item.getPicture() == null) {
            //The image isn't downloaded yet
            if (!item.isDownloading()){
                item.setDownloading(true);
                if (item.getPicUrl() != null)
                    ImagesDownloader.getInstance().getImage(ImagesDownloadedHandler.getInstance(),getListenerId(), position, item.getPicUrl());
            }
        }
        else {
           //Image already downloaded
            contenedor.pic.setImageBitmap(item.getPicture());
        }
        return view;
    }

    private void loadMoreItems() {
    }

    @Override
    public int getListenerId() {
        return CONST.IMAGECODE;
    }

    @Override
    public void notifyImageAvailable(int position, Object image) {
        Item item = (Item) getItem(position);
        if (item != null){
            item.setPicture((Bitmap)image);
            EndlessListListener.refreshView();
        }

    }

    class ContenedorView{
        TextView title;
        TextView subtitle;
        TextView price;
        TextView available_quantity;
        TextView condition;
        ImageView pic;
        //ProgressBar progress;
    }



}
