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
            db.execSQL("create table news(id text primary key, tag text, sim_json text, com_json text, star text)");
        } catch (Exception e){
            //Log.i(TAG, "init: ", e);
        }
        try{
            db.execSQL("create table tags(id text primary key)");
        } catch (Exception e){
            //Log.i(TAG, "init: ", e);
        }
    }

    public void addTag(String tag){
        ContentValues cValue = new ContentValues();
        cValue.put("id", tag);
        try{
            db.insert("tags", null, cValue);
        } catch (Exception e){
            Log.i(TAG, "addTag: ", e);
        }
    }

    public void delTag(String tag){
        try{
            db.delete("tags", "id=?", new String[]{tag});
        } catch (Exception e){
            Log.i(TAG, "delTag: ", e);
        }
    }

    public List<String> getTags(){
        List<String> result = new ArrayList<String>();
        Cursor cursor = db.query("tags", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            String tag = cursor.getString(0);
            result.add(tag);
        }
        return result;
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
        cValue.put("star", "NO");
        db.insert("news", null, cValue);
    }

    void insertCom(String jsonText) throws JSONException{
        Map<String, Object> newstosave = NewsManager.newsParser(jsonText);
        if(!exists((String)newstosave.get("news_ID"))){
            return;
        }
        Map<String, Object> oldone = get((String)newstosave.get("news_ID"));
        ContentValues cValues = new ContentValues();
        cValues.put("id", (String)oldone.get("id"));
        cValues.put("sim_json", (String)oldone.get("sim_json"));
        cValues.put("com_json", jsonText);
        cValues.put("star", "NO");
        db.update("news", cValues, "id=?", new String[]{(String)newstosave.get("news_ID")});
    }

    public void star(String news_ID){
        if(!exists(news_ID)){
            return;
        }
        Map<String, Object> oldone = get(news_ID);
        ContentValues cValues = new ContentValues();
        cValues.put("id", (String)oldone.get("id"));
        cValues.put("sim_json", (String)oldone.get("sim_json"));
        cValues.put("com_json", (String)oldone.get("com_json"));
        cValues.put("star", "YES");
        db.update("news", cValues, "id=?", new String[]{news_ID});
    }

    public void unstar(String news_ID){
        if(!exists(news_ID)){
            return;
        }
        Map<String, Object> oldone = get(news_ID);
        ContentValues cValues = new ContentValues();
        cValues.put("id", (String)oldone.get("id"));
        cValues.put("sim_json", (String)oldone.get("sim_json"));
        cValues.put("com_json", (String)oldone.get("com_json"));
        cValues.put("star", "NO");
        db.update("news", cValues, "id=?", new String[]{news_ID});
    }

    int count(){
        Cursor cursor = db.query("news", null, null, null, null, null, null);
        return cursor.getCount();
    }

    Map<String, Object> get(String news_ID){
        Map<String, Object> result = new HashMap<String, Object>();
        Cursor cursor = db.query("news", new String[]{"id", "sim_json", "com_json", "star"}, "id=?", new String[]{news_ID}, null, null, null);
        while(cursor.moveToNext()){
            result.put("id", cursor.getString(cursor.getColumnIndex("id")));
            result.put("sim_json", cursor.getString(cursor.getColumnIndex("sim_json")));
            result.put("com_json", cursor.getString(cursor.getColumnIndex("com_json")));
            result.put("star", cursor.getString(cursor.getColumnIndex("star")));
        }
        return result;
    }

    public List<Map<String, Object>> getStaredNews(){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Cursor cursor = db.query("news", new String[]{"id", "sim_json", "com_json", "star"}, "star=?", new String[]{"YES"}, null, null, null);
        while(cursor.moveToNext()){
            String jsonText = cursor.getString(cursor.getColumnIndex("com_json"));
            try{
                Map<String, Object> temp = NewsManager.newsParser(jsonText);
                result.add(temp);
            } catch (JSONException e){
                Log.i(TAG, "getStaredNews: ", e);
            }
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

    public boolean hasRead(String news_ID){
        Cursor cursor = db.query("news", new String[]{"id", "sim_json", "com_json", "star"}, "id=?", new String[]{news_ID}, null, null, null);
        if(cursor.getCount() < 1){
            return false;
        }
        while(cursor.moveToNext()){
            String jsonText = cursor.getString(cursor.getColumnIndex("com_json"));
            if(jsonText.equals("")){
                return false;
            }
        }
        return true;
    }

    public boolean isStared(String news_ID){
        Cursor cursor = db.query("news", new String[]{"id", "sim_json", "com_json", "star"}, "id=?", new String[]{news_ID}, null, null, null);
        if(cursor.getCount() < 1){
            return false;
        }
        while(cursor.moveToNext()){
            String jsonText = cursor.getString(cursor.getColumnIndex("star"));
            if(jsonText.equals("YES")){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public List<Map<String, Object>> getHistory(String tag) throws JSONException {
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
        db.execSQL("drop table tags");
        db.close();
    }
}
