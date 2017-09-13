package com.example.newsapp.model;

import android.util.Log;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
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
    }

    @Test
    public void allTest() throws Exception {
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        NewsManager manager = new NewsManager(mySqlite);
        manager.getSearchedNewsList("苹果", 1);
        List<Map<String, Object>> history = mySqlite.getHistory("科技");
        for (int i = 0; i < history.size(); ++i) {
            Log.i(TAG, "getNewsallTest: " + history.get(i).get("news_Title"));
            Map<String, Object> thenews = manager.getNews(history.get(i));
            Log.i(TAG, "getNewsallTest: " + i + thenews.get("news_Content"));
            thenews = manager.getNews(history.get(i));
            Log.i(TAG, "getNewsallTest: " + i + thenews.get("news_Content"));
        }
    }

    @Test
    public void starTest() throws Exception{
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        NewsManager manager = new NewsManager(mySqlite);
        List<Map<String, Object>> newslist = manager.getSearchedNewsList("北京", 1);
        for(int i = 0; i < newslist.size(); ++i){
            manager.getNews(newslist.get(i));
        }
        newslist = mySqlite.getStaredNews();
        Log.i(TAG, "starTestNum: " + newslist.size());
        Log.i(TAG, "starTest: " + mySqlite.isStared("201512280710cb9195663cd348198f7909eb91fc0156"));
        mySqlite.star("201512280710cb9195663cd348198f7909eb91fc0156");
        newslist = mySqlite.getStaredNews();
        Log.i(TAG, "starTestNum: " + newslist.size());
        Log.i(TAG, "starTest: " + mySqlite.isStared("201512280710cb9195663cd348198f7909eb91fc0156"));
        mySqlite.unstar("201512280710cb9195663cd348198f7909eb91fc0156");
        newslist = mySqlite.getStaredNews();
        Log.i(TAG, "starTest: " + mySqlite.isStared("201512280710cb9195663cd348198f7909eb91fc0156"));
        Log.i(TAG, "starTestNum: " + newslist.size());
    }

    @Test
    public void hasReadTest() throws Exception{
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        NewsManager manager = new NewsManager(mySqlite);
        List<Map<String, Object>> newslist = manager.getSearchedNewsList("北京", 1);
        Log.i(TAG, "hasReadTest: " + mySqlite.hasRead("201512280710cb9195663cd348198f7909eb91fc0156"));
        manager.getNews("201512280710cb9195663cd348198f7909eb91fc0156");
        Log.i(TAG, "hasReadTest: " + mySqlite.hasRead("201512280710cb9195663cd348198f7909eb91fc0156"));
    }

    @Test
    public void tagTest() throws Exception{
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        Log.i(TAG, "tagTest: 0 " + mySqlite.getTags().size());
        mySqlite.addTag("科技"); mySqlite.addTag("教育");
        Log.i(TAG, "tagTest: 2 " + mySqlite.getTags().size());
        mySqlite.delTag("教育");
        Log.i(TAG, "tagTest: 1 " + mySqlite.getTags().size());
    }

    @Test
    public void fileTest() throws Exception{
        File file = new File("/data/data/com.example.newsapp");
        Log.i(TAG, "fileTest: " + file.exists());
    }

    @Test
    public void pictureTest() throws Exception{
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        NewsManager manager = new NewsManager(mySqlite);
        List<Map<String, Object>> newslist = manager.getSearchedNewsList("tfboys", 1);
        Log.i(TAG, "pictureTest: " + mySqlite.getPicture("2016010607122c5799f8e0ae43019b4f1887717ec63e"));
    }
}