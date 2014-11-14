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
}
