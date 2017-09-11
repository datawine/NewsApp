package com.example.newsapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by leavan on 2017/9/11.
 */

public class MySqlite {
    private static final String TAG = "MySqlite";
    SQLiteDatabase db = null;

    MySqlite(){
    }

    void init(){
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.newsapp/news.db", null);
        try {
            db.execSQL("create table news(id integer primary key, tag text, sim_json text, com_json text)");
        } catch (Exception e){
            //Log.i(TAG, "init: ", e);
        }
    }

    void insert(){
        ContentValues cValue = new ContentValues();
        cValue.put("tag", "科技");
        cValue.put("sim_json", "小米mix2发布会");
        cValue.put("com_json", "雷军小米mix2发布会");
        db.insert("news", null, cValue);
    }

    int query(){
        Cursor cursor = db.query("news", null, null, null, null, null, null);
        return cursor.getCount();
    }
}
