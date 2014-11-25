package com.example.jerario.tutorial1.managers;

import android.util.Log;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by jerario on 11/11/14.
 * @description: Search items using MercadoLibre API
 */
public class ItemManager {
    private static final String TAG = "ITEM MANAGER";

    public LinkedList<Item> searchItems(String query, int offset, int limit) throws IOException, JSONException{
        Log.d(TAG,"searching items in the api");
        StringBuilder uriStringBuilder = new StringBuilder();
        //Creating query
        uriStringBuilder.append("https://api.mercadolibre.com/sites/MLA/search?q=");
        uriStringBuilder.append(URLEncoder.encode(query, "UTF-8"));
        uriStringBuilder.append("&limit=");
        uriStringBuilder.append(limit);
        uriStringBuilder.append("&offset=");
        uriStringBuilder.append(offset);
        String uriString = uriStringBuilder.toString();


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

    public static Item getItem(String id) throws IOException, JSONException{
        Item item = new Item();
        StringBuilder uriStringBuilder = new StringBuilder();
        uriStringBuilder.append("https://api.mercadolibre.com/items/");
        uriStringBuilder.append(id);
        String uriString = uriStringBuilder.toString();
        String response = JSONUtils.ejecuteQuery(uriString);
        String stringDate;
        Date date;

        JSONObject result = new JSONObject(response);

        item.setId(id);
        item.setTitle(result.getString("title"));
        item.setCondition(result.getString("condition"));
        item.setSubtitle(result.getString("subtitle"));
        item.setAvailable_quantity(result.getInt("available_quantity"));
        item.setPicUrl(result.getString("thumbnail"));
        item.setPrice(result.getDouble("price"));

        stringDate = result.getString("stop_time");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date convertedDate = dateFormat.parse(stringDate);
            item.setStopTime(convertedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        item.setPrice(result.getDouble("price"));
        return item;
    }


}
