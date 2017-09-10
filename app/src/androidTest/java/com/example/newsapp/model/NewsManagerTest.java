package com.example.newsapp.model;

import android.util.Log;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by leavan on 2017/9/10.
 */
public class NewsManagerTest {
    private static final String TAG = "getNewstest";

    @Test
    public void getNews() throws Exception {
        NewsManager manager = new NewsManager();
        //Log.i(TAG, "getNews: " + manager.getPage("http://166.111.68.66:2042/news/action/query/search?keyword=北京"));
        String page = manager.getPage("http://166.111.68.66:2042/news/action/query/search?keyword=北京");
        List<Map<String, Object>> parseresult = manager.newsListParser(page);
        for(int i = 0; i < parseresult.size(); ++i)
        {
            Map<String, Object> news = parseresult.get(i);
            Log.i(TAG, "getNews: " + (String)news.get("news_Title"));
        }

    }
    @Test
    public void getaNews() throws Exception{
        NewsManager manager = new NewsManager();
        Map<String, Object> news = manager.getNews("201608090432c815a85453c34d8ca43a591258701e9b");
        Log.i(TAG, "getaNews: " + (String)news.get("news_Content"));
    }

    @Test
    public void getLateNews() throws Exception{
        NewsManager manager = new NewsManager();
        for(int i = 0; i < 3; ++i) {
            List<Map<String, Object>> news1 = manager.getLatestNewsList("科技");
            for (int j = 0; j < news1.size(); ++j) {
                Map<String, Object> news = news1.get(j);
                Map<String, Object> detailed_news = manager.getNews((String)news.get("news_ID"));
                Log.i(TAG, "getLateNews: " + j + news.get("news_Title"));
                Log.i(TAG, "getLateNews: " + detailed_news.get("news_Content"));
            }
        }
    }

    @Test
    public void getSearchedNews() throws Exception{
        NewsManager manager = new NewsManager();
        for(int i = 0; i < 3; ++i) {
            List<Map<String, Object>> news1 = manager.getSearchedNewsList("科技", 1);
            for (int j = 0; j < news1.size(); ++j) {
                Map<String, Object> news = news1.get(j);
                Map<String, Object> detailed_news = manager.getNews((String)news.get("news_ID"));
                Log.i(TAG, "getSearchedNews: " + j + news.get("news_Title"));
                Log.i(TAG, "getSearchedNews: " + detailed_news.get("news_Content"));
            }
        }
    }
}