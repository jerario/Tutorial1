package com.example.jerario.tutorial1.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.jerario.tutorial1.R;
import com.example.jerario.tutorial1.ResultsActivity;
import com.example.jerario.tutorial1.adapters.ProductAdapter;
import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.tasks.SearchItemsTask;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by jerario on 11/12/14.
 */
public class EndlessListListener implements AbsListView.OnScrollListener {
    private static final Boolean DEBUGING = false;
    private static String TAG = "EndlessListListener";

    private int itemsPerPage = 15;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private String message;
    private Intent intent;
    public LinkedList<Item> productList;
    private ListView listResults;
    private static ProductAdapter adapter;
    private Context context;

    public EndlessListListener(){
        productList = new LinkedList<Item>();
    }


    public EndlessListListener(Intent intent, Context context, ListView view, LinkedList<Item> products) {
        this.intent = intent;
        this.context = context;
        this.listResults = view;
        productList = products;
        initComponents();
        if(!productList.isEmpty()) {
            showView(productList);
        }
        else{
            //Init List
            SearchItemsTask.searchItems(message, 0, 15, new Closure<LinkedList<Item>>() {
                @Override
                public void executeOnSuccess(LinkedList<Item> result) {
                    refreshView(result);
                }
            });
        }
        listResults.setOnScrollListener(this);
    }

    public void initComponents(){
        //Intent
        message = intent.getStringExtra(CONST.QUERYSTRING);
        adapter = new ProductAdapter(context,productList);
        listResults.setAdapter(adapter);

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount-1 > previousTotal) {
                loading = false;
                previousTotal = totalItemCount-1;
                currentPage++;
            }
        }
        if (DEBUGING) Log.d(TAG,"totalItemCount " +Integer.toString(totalItemCount-1));
        if (DEBUGING) Log.d(TAG,"visibleItemCount " +Integer.toString(visibleItemCount-1));
        if (DEBUGING) Log.d(TAG,"firstVisibleItem " +Integer.toString(firstVisibleItem-1));
        if (DEBUGING) Log.d(TAG,"itemsPerPage " +Integer.toString(CONST.VISIBLEITEMSHOLD));
    if (!loading && (totalItemCount-1 - visibleItemCount) <= (firstVisibleItem-1 + CONST.VISIBLEITEMSHOLD)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            if (DEBUGING) Log.d(TAG,"Searching MORE ITEMS");

            SearchItemsTask.searchItems(message, currentPage + 15, 15, new Closure<LinkedList<Item>>() {
                @Override
                public void executeOnSuccess(LinkedList<Item> result) {
                    refreshView(result);
                }
            });
            loading = true;
        }
    }

    public static void refreshView(){
        adapter.notifyDataSetChanged();
    }

    private void refreshView(LinkedList<Item> list){
        if (DEBUGING) Log.d(TAG, "In refresh view");
        //productList.clear();
        productList.addAll(list);
        adapter.notifyDataSetChanged();
        if (DEBUGING) Log.d(TAG, "Finishing refresh view");
    }

    public void showView(LinkedList<Item> list){
        productList = list;
        adapter.notifyDataSetChanged();
    }

    public LinkedList<Item> getProductList(){
        return productList;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }
}
