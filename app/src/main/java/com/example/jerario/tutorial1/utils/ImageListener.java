package com.example.jerario.tutorial1.utils;

/**
 * Created by jerario on 11/14/14.
 */
public interface ImageListener {
    int getListenerId();
    void notifyImageAvailable(int imageId,Object image);

}
