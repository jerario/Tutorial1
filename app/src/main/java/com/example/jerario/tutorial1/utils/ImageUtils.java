package com.example.jerario.tutorial1.utils;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.util.Log;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Some utils to work with images.
 * Created by jerario on 11/13/14.
 */
public class ImageUtils {
    private static final Boolean DEBUGING = true;
    private static final String TAG = "IMAGE UTILS";

    public static byte[] convertUrlToByte(String url){
        try {
            if (DEBUGING) Log.d(TAG, "Start Conversion from: " + url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpRequestBase request = new HttpGet(url);
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            if (DEBUGING) Log.d(TAG, "Conversion finished and entity is null: " + Boolean.toString(entity == null));
            return EntityUtils.toByteArray(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    public static Bitmap getResizedBitmap(String url, int newHeight, int newWidth){
        Bitmap bm = getBitmapFromURL(url);
        return getResizedBitmap(bm,newHeight,newWidth);
    }

}
