package com.example.jerario.tutorial1.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jerario.tutorial1.utils.CONST;

import java.util.HashMap;

/**
 *
 * Created by jerario on 11/21/14.
 */
public class TrackerSqlHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TrackerDatabase.db";
    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry.ID + " TEXT PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.PRICE + " DOUBLE" +
                    ")";

    private static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public TrackerSqlHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        onCreate(sqLiteDatabase);
    }

    public long trackItem(String id, double price){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.ID,id);
        values.put(FeedReaderContract.FeedEntry.PRICE,price+1);

        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values
            );
        return newRowId;
    }

    public double getPrice(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT "+FeedReaderContract.FeedEntry.PRICE+
                " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME+
                " WHERE " + FeedReaderContract.FeedEntry.ID + "=" + id;
        Cursor i = db.rawQuery(query,null);

        double price = CONST.QUERYISNULL;
        if(i.moveToFirst()){
            price = i.getDouble(0);
        }
        return price;
    }

    public void updatePrice(String id, double price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.PRICE,price);

        db.update(FeedReaderContract.FeedEntry.TABLE_NAME, values,
                FeedReaderContract.FeedEntry.ID +"= "+ id, null);
        db.close();
    }

    public void untrackItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.ID +"= '"+ id +"'", null);
        db.close();
    }

    public boolean isTracked(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[] {id};
        Cursor c = db.rawQuery(" SELECT "+ FeedReaderContract.FeedEntry.ID +
                " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME +
                " WHERE " + FeedReaderContract.FeedEntry.ID +"=? ", args);
        return c.moveToFirst();
    }

    public HashMap<String,Double> getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String,Double> items = new HashMap<String, Double>();
        String id;
        Double price;

        //Information need after the result of the query
        String[] projection = {
                FeedReaderContract.FeedEntry.ID,
                FeedReaderContract.FeedEntry.PRICE
        };

        //How to sort the information
        String sortOrder =
                FeedReaderContract.FeedEntry.ID + " DESC";

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (c.moveToNext()){
            id = c.getString(0);
            price = c.getDouble(1);
            items.put(id,price);
        }
        return items;
    }

}
