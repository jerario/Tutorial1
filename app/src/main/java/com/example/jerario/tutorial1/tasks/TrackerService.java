package com.example.jerario.tutorial1.tasks;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.managers.ItemManager;
import com.example.jerario.tutorial1.utils.TrackerNotification;
import com.example.jerario.tutorial1.utils.database.TrackerSqlHelper;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by jerario on 11/21/14.
 */
public class TrackerService extends IntentService {

    private static final String TAG = TrackerService.class.getName();
    private TrackerSqlHelper sql;
    private HashMap<String,Double> itemsTracked;
    private TrackerNotification trackerNotification;
    private long TIME_BEFORE_EXPIRE = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES);

    public TrackerService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"Started service");
        sql = new TrackerSqlHelper(getApplicationContext());
        trackerNotification = new TrackerNotification(getApplicationContext());
        itemsTracked = sql.getAllData();
        String id;
        Double price;
        long expireDate, actualDate;
        Item item;

        for(HashMap.Entry<String,Double> entry : itemsTracked.entrySet()){
            id = entry.getKey();
            price = entry.getValue();
            try {
                item = ItemManager.getItem(id);
                Log.d(TAG,"Checking status of " + item.getId());
                Log.d(TAG,"Saved price " + Double.toString(price));
                Log.d(TAG,"Actual price " + Double.toString(item.getPrice()));

                //Check if price changed
                if(price != item.getPrice()){
                    //Price changed
                    Log.d(TAG,item.getId() + "price changed");
                    trackerNotification.ItemNotification(TrackerNotification.NotificationType.PRICE_CHANGED,
                            item);
//                    sql.updatePrice(id, item.getPrice());
                }
                expireDate = item.getStopTime();
                actualDate = System.currentTimeMillis();
                //Check if it is expired
                if (actualDate>=expireDate){
                    Log.d(TAG,item.getId() + "expired");
                    trackerNotification.ItemNotification(TrackerNotification.NotificationType.EXPIRED,
                            item);
                    sql.untrackItem(id);
                }
                //Check if it is about to expire
                if(actualDate + TIME_BEFORE_EXPIRE >= expireDate){
                    Log.d(TAG,item.getId() + "abouttoExpire");

                    trackerNotification.ItemNotification(TrackerNotification.NotificationType.ABOUT_TO_EXPIRE,
                            item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Check if the price changed




    }
}
