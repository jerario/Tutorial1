package com.example.jerario.tutorial1.utils.database;

import android.provider.BaseColumns;

/**
 * Created by jerario on 11/20/14.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "tracker";
        public static final String ID = "_id";
        public static final String PRICE = "price";
    }
}