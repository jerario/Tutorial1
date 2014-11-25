package com.example.jerario.tutorial1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerario.tutorial1.entities.Item;


public class VIPViewActivity extends Activity{
    private final String TAG = "VIP VIEW ACTIVITY";
    private Item item;
    TextView titleView;
    TextView subTitleView;
    TextView priceView;
    TextView conditionView;
    TextView stockView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_container);


        VipViewFragment vipFragment = (VipViewFragment) getFragmentManager().findFragmentByTag(VipViewFragment.TAG);
        if (vipFragment == null){
            vipFragment = VipViewFragment.newInstance(savedInstanceState);
            getFragmentManager().beginTransaction().replace(R.id.results_container, vipFragment, VipViewFragment.TAG).commit();
        }



    }
}
