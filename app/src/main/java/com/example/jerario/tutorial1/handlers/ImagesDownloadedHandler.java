package com.example.jerario.tutorial1.handlers;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.ImageListener;

import java.util.HashMap;

/**
 * Image Downloader Handler, send a message to the main thread when the downloading of images
 * finished
 * Created by jerario on 11/14/14.
 */
public class ImagesDownloadedHandler extends Handler {
    private static final Boolean DEBUGING = false;
    private static final String TAG = "ImagesDownloaderHandler";

    private static ImagesDownloadedHandler ourInstance = new ImagesDownloadedHandler();
    private HashMap<Integer,ImageListener> imageListenerHash;

    public static ImagesDownloadedHandler getInstance() {
        return ourInstance;
    }

    private ImagesDownloadedHandler() {
        imageListenerHash = new HashMap<Integer, ImageListener>();
    }

    @Override
    public void handleMessage(Message msg){
        if (msg.what != CONST.IMAGEDOWNLOADED){
            return;
        }
        Bitmap image = (Bitmap) msg.obj;
        int imageListenerId = msg.arg1;
        int imageId = msg.arg2;
        ImageListener listener = imageListenerHash.get(imageListenerId);
        listener.notifyImageAvailable(imageId,image);
    }

    public void registerListener(ImageListener listener){
        imageListenerHash.put(listener.getListenerId(),listener);
    }
}
