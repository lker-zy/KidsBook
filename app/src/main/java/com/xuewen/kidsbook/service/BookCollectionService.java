package com.xuewen.kidsbook.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.KidsBookApplication;
import com.xuewen.kidsbook.db.BookCollectionDatabaseHelper;
import com.xuewen.kidsbook.service.beans.BookCollection;
import com.xuewen.kidsbook.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

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

    public static void init() {
        BookCollectionDatabaseHelper dbHelper = new BookCollectionDatabaseHelper(KidsBookApplication.getInstance().getContext(),
                TABLE, null, AppConfig.DB_VERSION_4_BOOK_COLLECTION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();
    }

    public void add(BookCollection bookCollection) {

        BookCollectionDatabaseHelper dbHelper = new BookCollectionDatabaseHelper(context, TABLE, null, AppConfig.DB_VERSION_4_BOOK_COLLECTION);
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

    public List<BookCollection> list() {
        BookCollectionDatabaseHelper dbHelper = new BookCollectionDatabaseHelper(context, TABLE, null, AppConfig.DB_VERSION_4_BOOK_COLLECTION);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<BookCollection> collectionsList = new ArrayList<>();
        /*
        Cursor cursor = db.query(TABLE, new String[] {"name", "author", "words"}, null, null, null, null, null, null);
        */

        String sql = " select name, author, words, price from " + TABLE;
        LogUtil.d(TAG, "sql is: " + sql);

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            BookCollection collection = new BookCollection();
            collection.setName(cursor.getString(cursor.getColumnIndex("name")));
            collection.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            collection.setWords(cursor.getLong(cursor.getColumnIndex("words")));
            collection.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));

            collectionsList.add(collection);
        }

        return collectionsList;
    }

    public int count() {
        int count = 0;

        BookCollectionDatabaseHelper dbHelper = new BookCollectionDatabaseHelper(context, TABLE, null, AppConfig.DB_VERSION_4_BOOK_COLLECTION);
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
