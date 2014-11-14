package com.example.jerario.tutorial1.entities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * Entity for Item
 * Created by jerario on 11/11/14.
 */

public class Item implements Serializable{
    private String id;
    private String title;
    private String subtitle;
    private String seller;
    private String category;
    private double price;
    private int available_quantity;
    private int sold_quantity;
    private String condition;
    private String description;
    private String picUrl;
    private transient boolean isDownloading;
    private transient Bitmap picture;


    public Item(){

    }

    public Item(String title) {
        this.title = title;
    }

    public Item(String id, String title, String subtitle, String seller, String category, double price, int available_quantity, int sold_quantity, String condition, String description) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.seller = seller;
        this.category = category;
        this.price = price;
        this.available_quantity = available_quantity;
        this.sold_quantity = sold_quantity;
        this.condition = condition;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        if (subtitle == "null")
            subtitle = null;
        else
            this.subtitle = subtitle;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(int available_quantity) {
        this.available_quantity = available_quantity;
    }

    public int getSold_quantity() {
        return sold_quantity;
    }

    public void setSold_quantity(int sold_quantity) {
        this.sold_quantity = sold_quantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() { return picUrl; }

    public void setPicUrl(String pic) { this.picUrl = pic; }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
