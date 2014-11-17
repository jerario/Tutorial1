package com.example.jerario.tutorial1.managers;

import android.util.Log;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.utils.JSONUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;

/**
 * Created by jerario on 11/11/14.
 * @description: Search items using MercadoLibre API
 */
public class ItemManager {
    private final String TAG = "ITEM MANAGER";

    public LinkedList<Item> searchItems(String query, int offset, int limit) throws IOException, JSONException{
        StringBuilder uriStringBuilder = new StringBuilder();
        //Creating query
        uriStringBuilder.append("https://api.mercadolibre.com/sites/MLA/search?q=");
        uriStringBuilder.append(URLEncoder.encode(query, "UTF-8"));
        uriStringBuilder.append("&limit=");
        uriStringBuilder.append(limit);
        uriStringBuilder.append("&offset=");
        uriStringBuilder.append(offset);
        String uriString = uriStringBuilder.toString();

        Log.d("ITEMMANAGER",uriString);

        //Executing query

        String responseStr = JSONUtils.ejecuteQuery(uriString);
        JSONObject result = new JSONObject(responseStr);
        //Creating list of products
        LinkedList<Item> itemList = new LinkedList<Item>();
        JSONArray resultJsArray = result.getJSONArray("results");
        JSONObject actualObject;
        for (int i = 0; i < resultJsArray.length() ; i++) {
            Item item = new Item();
            actualObject = resultJsArray.getJSONObject(i);
            item.setId(actualObject.getString("id"));
            item.setTitle(actualObject.getString("title"));
            item.setCondition(actualObject.getString("condition"));
            item.setSubtitle(actualObject.getString("subtitle"));
            item.setAvailable_quantity(actualObject.getInt("available_quantity"));
            item.setPicUrl(actualObject.getString("thumbnail"));
            Log.d(TAG,"SETING THUMB "+item.getPicUrl());
            item.setPrice(actualObject.getDouble("price"));
            /*JSONArray qualityPictures = getJSONPictures(actualObject.getString("id"));
            for (int j = 0; j<qualityPictures.length();j++){
                JSONObject jsitem = qualityPictures.getJSONObject(j);
                item.addQuality_pictureUrl(jsitem.getString("url"));
            }*/
            itemList.add(item);
        }

        return itemList;
    }

    public static JSONArray getJSONPictures(String id) throws IOException, JSONException{
        StringBuilder uriStringBuilder = new StringBuilder();
        uriStringBuilder.append("https://api.mercadolibre.com/items/");
        uriStringBuilder.append(id);
        String uriString = uriStringBuilder.toString();
        String response = JSONUtils.ejecuteQuery(uriString);
        JSONObject result = new JSONObject(response);
        JSONArray resultJsArray = result.getJSONArray("pictures");
        return resultJsArray;
    }



}
