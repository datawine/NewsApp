package com.example.newsapp;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;

import com.example.newsapp.model.MySqlite;
import com.example.newsapp.model.NewsManager;
import com.example.newsapp.view.briefinfo.BriefInfoActivity;
import com.example.newsapp.view.briefinfo.PageListFragment;

import org.json.JSONException;

import java.util.ArrayList;
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

    public String[] GetTags()
    {
        List<String> tags = new ArrayList<String>();

        tags.add("推荐");
        tags.add("收藏夹");

        List<String> tmp = mySqlite.getTags();

        for(int i=0;i<tmp.size();i++)
        tags.add(tmp.get(i));

        String[] tagstring = tags.toArray(new String[tags.size()]);

        return tagstring;
    }

    public void AddTag(String key)
    {
        mySqlite.addTag(key);

        BriefInfoActivity bri = BriefInfoActivity.getInstance();

        bri.onResume();
    }

    public void DelTag(String key)
    {
        mySqlite.delTag(key);

        BriefInfoActivity bri = BriefInfoActivity.getInstance();

        bri.onResume();
    }
}
