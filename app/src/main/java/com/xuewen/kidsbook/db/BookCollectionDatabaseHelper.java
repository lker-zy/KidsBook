package com.xuewen.kidsbook.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lker_zy on 16-4-30.
 */
public class BookCollectionDatabaseHelper extends SQLiteOpenHelper {
    private static String TAG = BookCollectionDatabaseHelper.class.getSimpleName();
    private static String TABLE = "book_collection";

    public BookCollectionDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BookCollectionDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (id integer primary key, name varchar(128), author varchar(36), publisher varchar(36))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

