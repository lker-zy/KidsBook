package com.xuewen.kidsbook.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xuewen.kidsbook.db.BookCollectionDatabaseHelper;
import com.xuewen.kidsbook.service.beans.BookCollection;
import com.xuewen.kidsbook.utils.LogUtil;

/**
 * Created by lker_zy on 16-4-30.
 */
public class BookCollectionService {
    private static String TAG = BookCollectionService.class.getSimpleName();
    private static String TABLE = "book_collection";

    private Context context;

    public BookCollectionService(Context context) {
        this.context = context;
    }

    public void add(BookCollection bookCollection) {

        BookCollectionDatabaseHelper dbHelper = new BookCollectionDatabaseHelper(context, TABLE, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*
        String addSql = "INSERT INTO " + TABLE + "(id, name, author) values(" + bookCollection.getId() + ", '" +
                bookCollection.getName() + "', '" + bookCollection.getAuthor() + "')";
        LogUtil.d(TAG, "sql is: " + addSql);

        db.execSQL(addSql);
        */

        LogUtil.d(TAG, "insert book collection: " + bookCollection.getContentValues().toString());
        db.insert(TABLE, null, bookCollection.getContentValues());
        db.close();
    }

    public int count() {
        int count = 0;

        BookCollectionDatabaseHelper dbHelper = new BookCollectionDatabaseHelper(context, TABLE, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String addSql = "select count(*) from " + TABLE;
        LogUtil.d(TAG, "sql is: " + addSql);

        Cursor cursor = db.rawQuery(addSql, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0) ;
        }
        cursor.close();

        db.close();

        return count;
    }
}
