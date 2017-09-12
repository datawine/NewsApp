package com.example.newsapp.model;

import android.util.Log;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by leavan on 2017/9/11.
 */
public class MySqliteTest {
    private static final String TAG = "MySqliteTest";

    @Test
    public void initTest() throws Exception{
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        NewsManager manager = new NewsManager(mySqlite);
        for(int i = 0; i < 4; ++i) {
            List<Map<String, Object>> news1 = manager.getLatestNewsList();
            for (int j = 0; j < news1.size(); ++j) {
                Map<String, Object> news = news1.get(j);
            }
        }
        Log.i(TAG, "initTest: " + mySqlite.count());
        List<Map<String, Object>> query = mySqlite.getHistory("科技");
        for(int i = 0; i < query.size(); ++i){
            Log.i(TAG, "initTest: " + query.get(i).get("news_Title"));
        }
        //mySqlite.delete();
    }

    @Test
    public void allTest() throws Exception{
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        NewsManager manager = new NewsManager(mySqlite);
        //manager.getSearchedNewsList("苹果", 1);
        List<Map<String, Object>> history = mySqlite.getHistory("科技");
        for(int i = 0; i < history.size(); ++i){
            Log.i(TAG, "allTest: " + history.get(i).get("news_Title"));
            Map<String, Object> thenews = manager.getNews(history.get(i));
            Log.i(TAG, "allTest: " + i + thenews.get("news_Content"));
        }
    }
}