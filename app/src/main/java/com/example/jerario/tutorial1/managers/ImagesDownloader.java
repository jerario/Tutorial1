package com.example.jerario.tutorial1.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.ImageUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jerario on 11/13/14.
 */
public class ImagesDownloader {
    private static final Boolean DEBUGING = true;
    private static final String TAG = "Image Downloader";
    private static ImagesDownloader ourInstance = new ImagesDownloader();
    private LruCache<String,Bitmap> imagesCache;
    private ThreadPoolExecutor threadPoolExecutor;


    public static ImagesDownloader getInstance() {
        return ourInstance;
    }

    private ImagesDownloader() {
        int cacheSize = 8 * 1024 * 1024; // 8Mb Cache
        imagesCache = new LruCache<String,Bitmap>(cacheSize){
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }
        };
        threadPoolExecutor = new ThreadPoolExecutor(2,5,20, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
    }

    public void getImage(final Handler handler, final int contentId, final int imageId ,final String imageUrl){
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    if (imageUrl == null || imageUrl.isEmpty())
                        return ;
                    //Searching if it exist in cache
                    Bitmap image = imagesCache.get(imageUrl);
                    if (image == null) {
                        //   byte[] content = getContent(imageUrl);
                        byte[] content = ImageUtils.convertUrlToByte(imageUrl);
                        image = BitmapFactory.decodeByteArray(content, 0, content.length);
                        imagesCache.put(imageUrl, image);
                    }
                      //  final BitmapFactory.Options options = new BitmapFactory.Options();
                      //  options.inJustDecodeBounds = true;
                       // BitmapFactory.decodeResource(imageUrl,imageUrl,options);
                        Message msg = Message.obtain(handler, CONST.IMAGEDOWNLOADED,contentId,imageId,image);
                        msg.sendToTarget();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    public void getImageUrl(final Handler handler,final Item item){
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONArray qualityPictures = ItemManager.getJSONPictures(item.getId());
                    for (int j = 0; j < qualityPictures.length(); j++) {
                        JSONObject jsitem = qualityPictures.getJSONObject(j);
                        item.addQuality_pictureUrl(jsitem.getString("url"));
                    }
                    Message msg = Message.obtain(handler, CONST.URLMSG,item);
                    msg.sendToTarget();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });    }


}
