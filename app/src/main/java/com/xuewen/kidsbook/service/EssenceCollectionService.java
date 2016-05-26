package com.xuewen.kidsbook.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.db.EssenceCollectionDatabaseHelper;
import com.xuewen.kidsbook.service.beans.EssenceCollection;
import com.xuewen.kidsbook.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lker_zy on 16-4-29.
 */
public class EssenceCollectionService {
    private static String TAG = EssenceCollectionService.class.getSimpleName();
    private static String TABLE = "essence_collection";

    private Context context;

    public EssenceCollectionService(Context context) {
        this.context = context;
    }

    public void add(EssenceCollection essenceCollection) {

        EssenceCollectionDatabaseHelper dbHelper = new EssenceCollectionDatabaseHelper(context, TABLE, null, AppConfig.DB_VERSION_4_ESSENCE_COLLECTION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*
        String addSql = "INSERT INTO " + TABLE + "(id, title, author) values(" + essenceCollection.getId() + ", '" +
                essenceCollection.getTitle() + "', '" + essenceCollection.getAuthor() + "')";
        LogUtil.d(TAG, "sql is: " + addSql);

        db.execSQL(addSql);
        */

        LogUtil.d(TAG, "insert book collection: " + essenceCollection.getContentValues().toString());
        db.insert(TABLE, null, essenceCollection.getContentValues());
        db.close();
    }

    public int getCount() {
        int count = 0;
        EssenceCollectionDatabaseHelper dbHelper = new EssenceCollectionDatabaseHelper(context, TABLE, null, AppConfig.DB_VERSION_4_ESSENCE_COLLECTION);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String countSql = " select count(*) from " + TABLE;
        LogUtil.d(TAG, "sql is: " + countSql);

        Cursor cursor = db.rawQuery(countSql, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        db.close();
        return count;
    }

    public List<EssenceCollection> list() {
        List<EssenceCollection> list = new ArrayList<>();
        EssenceCollectionDatabaseHelper dbHelper = new EssenceCollectionDatabaseHelper(context, TABLE, null, AppConfig.DB_VERSION_4_ESSENCE_COLLECTION);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = " select id, title, author from " + TABLE;
        LogUtil.d(TAG, "sql is: " + sql);

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            EssenceCollection collection = new EssenceCollection();
            collection.setId(cursor.getInt(0));
            collection.setTitle(cursor.getString(1));
            collection.setAuthor(cursor.getString(2));

            list.add(collection);
        }

        cursor.close();

        db.close();

        return list;
    }
}
