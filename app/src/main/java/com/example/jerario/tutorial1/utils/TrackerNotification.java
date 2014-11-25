package com.example.jerario.tutorial1.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.jerario.tutorial1.R;
import com.example.jerario.tutorial1.VIPViewActivity;
import com.example.jerario.tutorial1.entities.Item;

/**
 * Custom Notification for items.
 * Created by jerario on 11/21/14.
 */
public class TrackerNotification {
    private Context context;

    public enum NotificationType {
        EXPIRED, ABOUT_TO_EXPIRE, PRICE_CHANGED
    }

    public TrackerNotification(Context context){
        this.context = context;
    }

    public void ItemNotification(NotificationType type, Item item){
        Intent viewIntent = new Intent(context, VIPViewActivity.class);
        viewIntent.putExtra(CONST.ITEM, item);
        viewIntent.putExtra(CONST.CALLER,CONST.FROMNOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,viewIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setContentTitle(item.getTitle())
                .setTicker(item.getTitle())
                .setAutoCancel(true);

        switch (type) {
            case EXPIRED:
                notif.setContentText(context.getString(R.string.expired));
                break;
            case ABOUT_TO_EXPIRE:
                notif.setContentText(context.getString(R.string.aboutExpire));
                break;
            case PRICE_CHANGED:
                notif.setContentText(context.getString(R.string.priceChanged));
                break;
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(item.hashCode(),notif.build());
    }
}
