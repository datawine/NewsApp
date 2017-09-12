package com.example.newsapp;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;

import com.example.newsapp.model.MySqlite;
import com.example.newsapp.model.NewsManager;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * Created by junxian on 9/9/2017.
 */

public class MyApplication extends Application {
    private boolean dayMode;
    UiModeManager mUiModeManager;

    private MySqlite mySqlite;
    private NewsManager newsManager;

    private String[] tags = {"科技","教育","军事","国内","社会", "文化", "汽车","国际","体育","财经","健康","娱乐"};

    private
    static MyApplication instance;

    public
    static MyApplication getInstance() {

        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDayMode(true);
        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        instance = this;


                mySqlite = new MySqlite();
                mySqlite.init();

                newsManager = new NewsManager(mySqlite);


                for(int i=0;i<tags.length;i++)
                {
                    for(int j=0;j<2;j++) {
                try {
                    newsManager.getSearchedNewsList(tags[i], j);
                }catch(Exception e)
                {}

            }
        }
    }

    public boolean getDayMode() {
        return dayMode;
    }

    public void setDayMode(boolean mode) {
        dayMode = mode;
    }

    public List<Map<String, Object>> GetLatestNewsList() throws InterruptedException
    {
        return newsManager.getLatestNewsList();
    }

    public List<Map<String, Object>> GetSearchNewsList(String tag)  throws InterruptedException
    {
        return newsManager.getSearchedNewsList(tag,1);
    }

    public Map<String,Object> GetNews(String ID) throws InterruptedException, JSONException
    {
        return newsManager.getNews(ID);
    }

    public List<Map<String, Object>> GetStarNews()
    {
        return mySqlite.getStaredNews();
    }

    public List<Map<String, Object>> GetKeyWords(String ID) throws InterruptedException, JSONException
    {
        return (List<Map<String, Object>>) newsManager.getNews(ID).get("Keywords");
    }
}
