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


public class ResultsActivity extends Activity implements ResultsFragment.IOnClick {
    public static final String TAG = ResultsActivity.class.getName();
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
        fm = getFragmentManager();


        VipViewFragment vip = (VipViewFragment) getFragmentManager().findFragmentById(R.id.vip_fragment);
        mDualPane = !(vip == null || !vip.isInLayout());

        if (fm.findFragmentByTag(ResultsFragment.TAG) == null) {
            if (getIntent() != null) {
                message = getIntent().getStringExtra(CONST.QUERYSTRING);
                resultsFragment = ResultsFragment.newInstance(savedInstanceState, message);
                fm.beginTransaction().replace(R.id.results_container, resultsFragment, ResultsFragment.TAG).commit();
            }
        }  
    }

    private void showVipView(Item item) {
        Intent intent = new Intent(this, VIPViewActivity.class);
        intent.putExtra(CONST.PRODUCTLIST,productList);
        intent.putExtra(CONST.ITEM, item);
        startActivity(intent);
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


    public void setProductList(LinkedList<Item> productList){
        this.productList = productList;
    }
}
