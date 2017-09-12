package com.example.newsapp.model;

import android.util.Log;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by leavan on 2017/9/10.
 */
public class NewsManagerTest {
    private static final String TAG = "NewsManagerTest";

    @Test
    public void getPageTest() throws Exception {
        NewsManager manager = new NewsManager();
        String page = manager.getPage("http://166.111.68.66:2042/news/action/query/search?keyword=北京");
        List<Map<String, Object>> parseresult = manager.newsListParser(page);
        for(int i = 0; i < parseresult.size(); ++i)
        {
            Map<String, Object> news = parseresult.get(i);
            Log.i(TAG, "getPageTest: " + (String)news.get("news_Title"));
        }

    }

    @Test
    public void getNewsTest() throws Exception{
        NewsManager manager = new NewsManager();
        Map<String, Object> news = manager.getNews("201608090432c815a85453c34d8ca43a591258701e9b");
        Log.i(TAG, "getNewsTest: " + (String)news.get("news_Content"));
    }

    @Test
    public void getLateNewsTest() throws Exception{
        NewsManager manager = new NewsManager();
        for(int i = 0; i < 3; ++i) { //调用三次getLatestNewsList， 返回结果不一样
            List<Map<String, Object>> news1 = manager.getLatestNewsList("科技");
            for (int j = 0; j < news1.size(); ++j) {
                Map<String, Object> news = news1.get(j);
                Map<String, Object> detailed_news = manager.getNews((String)news.get("news_ID"));
                Log.i(TAG, "getLateNewsTest: " + j + news.get("news_Title"));
                Log.i(TAG, "getLateNewsTest: " + detailed_news.get("news_Content"));
            }
        }
    }

    @Test
    public void getSearchedNewsTest() throws Exception{
        NewsManager manager = new NewsManager();
        List<Map<String, Object>> news1 = manager.getSearchedNewsList("科技", 1);
        for (int j = 0; j < news1.size(); ++j) {
            Map<String, Object> news = news1.get(j);
            Map<String, Object> detailed_news = manager.getNews((String)news.get("news_ID"));
            Log.i(TAG, "getSearchedNewsTest: " + j + news.get("news_Title"));
            Log.i(TAG, "getSearchedNewsTest: " + detailed_news.get("news_Content"));
        }
    }

    @Test
    public void getPicNumTest() throws Exception{
        NewsManager manager = new NewsManager();
        Map<String, Object> news = manager.getNews("201509210711832206078244433fae36f4cdb1c24fbb");
        Log.i(TAG, "getPicNumTest: " + (Integer)news.get("pic_Num"));
    }

    @Test
    public void getFileTest() throws Exception{
        File file = new File("/data/");
        if(file.exists()){
            Log.i(TAG, "getFileTest: " + "YES");
            String[] files = file.list();
            Log.i(TAG, "getFileTest: " + files);
            //for(int i = 0; i < files.length; ++i){
             //   Log.i(TAG, "getFileTest: " + files[i]);
            //}
        } else {
            Log.i(TAG, "getFileTest: " + "NO");
        }
    }
}