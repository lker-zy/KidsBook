package com.xuewen.kidsbook.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lker_zy on 16-5-28.
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static String TAG = UserDatabaseHelper.class.getSimpleName();
    private static String TABLE = "user";

    public UserDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UserDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE +
                " (id integer primary key, category_id int, words bigint, name varchar(128), " +
                "price float, " +
                "author varchar(36), publisher varchar(36), category_name varchar(32), desc varchar(65536) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
