package com.example.newsapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leavan on 2017/9/11.
 */

public class MySqlite {
    private static final String TAG = "MySqlite";
    SQLiteDatabase db = null;

    public MySqlite(){
    }

    public void init(){
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.newsapp/news.db", null);
        try {
            db.execSQL("create table news(id text primary key, tag text, sim_json text, com_json text)");
        } catch (Exception e){
            //Log.i(TAG, "init: ", e);
        }
    }

    void insert(String jsonText) throws JSONException {
        Map<String, Object> newstosave = NewsManager.simNewsParser(jsonText);
        if(exists((String)newstosave.get("news_ID"))){
            return;
        }
        ContentValues cValue = new ContentValues();
        cValue.put("id", (String)newstosave.get("news_ID"));
        cValue.put("tag", (String)newstosave.get("newsClassTag"));
        cValue.put("sim_json", jsonText);
        cValue.put("com_json", "");
        db.insert("news", null, cValue);
    }

    void insertCom(String jsonText) throws JSONException{
        Map<String, Object> newstosave = NewsManager.newsParser(jsonText);
        if(exists((String)newstosave.get("news_ID"))){
            return;
        }
        Map<String, Object> oldone = get((String)newstosave.get("news_ID"));
        ContentValues cValues = new ContentValues();
        cValues.put("id", (String)oldone.get("id"));
        cValues.put("sim_json", (String)oldone.get("sim_json"));
        cValues.put("com_json", jsonText);
        db.update("news", cValues, "id=?", new String[]{(String)newstosave.get("news_ID")});
    }

    int count(){
        Cursor cursor = db.query("news", null, null, null, null, null, null);
        return cursor.getCount();
    }

    Map<String, Object> get(String news_ID){
        Map<String, Object> result = new HashMap<String, Object>();
        Cursor cursor = db.query("news", new String[]{"id", "sim_json", "com_json"}, "id=?", new String[]{news_ID}, null, null, null);
        while(cursor.moveToNext()){
            result.put("id", cursor.getString(cursor.getColumnIndex("id")));
            result.put("sim_json", cursor.getString(cursor.getColumnIndex("sim_json")));
            result.put("com_json", cursor.getString(cursor.getColumnIndex("com_json")));
        }
        return result;
    }

    boolean exists(String news_ID){
        Cursor cursor = db.query("news", null, "id=?", new String[]{news_ID}, null, null, null);
        if(cursor.getCount() > 0){
            return true;
        } else {
            return false;
        }
    }

    List<Map<String, Object>> getHistory(String tag) throws JSONException {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Cursor cursor = db.query("news", new String[]{"sim_json"}, "tag=?", new String[]{tag}, null, null, null);
        while(cursor.moveToNext()){
            String jsonText = cursor.getString(cursor.getColumnIndex("sim_json"));
            Map<String, Object> temp = NewsManager.simNewsParser(jsonText);
            result.add(temp);
        }
        return result;
    }

    void delete(){
        db.execSQL("drop table news");
    }
}
