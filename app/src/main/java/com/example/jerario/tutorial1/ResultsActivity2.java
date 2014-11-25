package com.example.jerario.tutorial1;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.EndlessListListener;

import java.util.LinkedList;


public class ResultsActivity2 extends Activity implements ResultsFragment.IOnClick {
    public static final String TAG = ResultsActivity2.class.getName();
    private String message;
    private LinkedList<Item> productList = new LinkedList<Item>();
    private Item selectedItem;
    private EndlessListListener listListener;
    private FragmentManager fm;
    private VipViewFragment vipFragment;
    private ResultsFragment resultsFragment;
    boolean mDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_container);

        message = getIntent().getStringExtra(CONST.QUERYSTRING);
        if(savedInstanceState!=null) {
            selectedItem = (Item) savedInstanceState.getSerializable(CONST.ITEM);
            productList = (LinkedList<Item>) savedInstanceState.getSerializable(CONST.PRODUCTLIST);
        }
        fm = getFragmentManager();

        View vipFrame = findViewById(R.id.vip_fragment);
        mDualPane = vipFrame != null && vipFrame.getVisibility() == View.VISIBLE;
        resultsFragment = ResultsFragment.newInstance(savedInstanceState, message);
        vipFragment = VipViewFragment.newInstance(savedInstanceState);

        if (mDualPane) {
            if (selectedItem==null) {
                Log.d(TAG, "VERTICAL sin selected");
                fm.beginTransaction().replace(R.id.results_container, resultsFragment, ResultsFragment.TAG).commit();
            }
            else {
                Log.d(TAG, "VERTICAL con selected");
                fm.beginTransaction().replace(R.id.results_container, vipFragment, VipViewFragment.TAG).commit();

               // showVipView(selectedItem);
            }
        } else {
            Log.d(TAG, "HORIZONTAL");
            fm.beginTransaction().replace(R.id.result_fragment, resultsFragment, ResultsFragment.TAG).commit();
            //if (selectedItem == null) {
                Log.d(TAG, "vip Fragment con vip fragment Inflao");
                fm.beginTransaction().replace(R.id.vip_fragment, vipFragment, VipViewFragment.TAG).commit();
           // }
        }

    }

    private void showVipView(Item item) {
        Intent intent = new Intent(this, VIPViewActivity.class);
        intent.putExtra(CONST.PRODUCTLIST,productList);
        intent.putExtra(CONST.ITEM, item);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(Item item, LinkedList<Item> productList) {
        selectedItem = item;
        this.productList = productList;
        if (mDualPane) {
            vipFragment = (VipViewFragment) getFragmentManager().findFragmentById(R.id.vip_fragment);
            vipFragment.updateView(item);
        } else {
            showVipView(item);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CONST.ITEM, selectedItem);
        outState.putSerializable(CONST.PRODUCTLIST, productList);
    }

    public void setProductList(LinkedList<Item> productList){
        this.productList = productList;
    }
}
