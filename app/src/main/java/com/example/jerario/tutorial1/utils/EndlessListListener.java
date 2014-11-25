package com.example.jerario.tutorial1.utils;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.jerario.tutorial1.adapters.ProductAdapter;
import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.tasks.SearchItemsTask;

import java.util.LinkedList;

/**
 * Listener that send more request when the bottom of the list is reached
 * Created by jerario on 11/12/14.
 */
public class EndlessListListener implements AbsListView.OnScrollListener {
    private static final Boolean DEBUGING = true;
    private static String TAG = "EndlessListListener";

    private int itemsPerPage = 15;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = false;
    private String message;
    public LinkedList<Item> productList;
    private ListView listResults;
    private static ProductAdapter adapter;
    private Context context;

    public EndlessListListener(){
        productList = new LinkedList<Item>();
    }


    public EndlessListListener(String message, Context context, ListView view, LinkedList<Item> products) {
        this.message = message;
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
     //   message =
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
        if (!loading && (totalItemCount-1 - visibleItemCount) <= (firstVisibleItem-1 + CONST.VISIBLEITEMSHOLD)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.

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
        //productList.clear();
        productList.addAll(list);
        adapter.notifyDataSetChanged();
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
