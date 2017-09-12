package com.example.newsapp.model;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by leavan on 2017/9/10.
 */

public class NewsManager {
    private static final String TAG = "newsParser";
    private static final HashMap<String, Integer> tag2int = new HashMap<String, Integer>(){{
        put("推荐", 0);put("科技", 1);put("教育", 2);put("军事", 3);
        put("国内", 4);put("社会", 5);put("文化", 6);put("汽车", 7);
        put("国际", 8);put("体育", 9);put("财经", 10);put("健康", 11);
        put("娱乐", 12);
    }};
    private int[] tagPageNum = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final String baseUrl = "http://166.111.68.66:2042/news/action/query";
    private String targetUrl = null;
    private String jsonText = null;
    MySqlite mydb = null;

    public NewsManager(MySqlite db){
        this.mydb = db;
    }
    public NewsManager(){

    }

    public List<Map<String, Object>> getSearchedNewsList(String keyWord, int pageNum) throws InterruptedException {
        return newsListParser(getPage(baseUrl + "/search?keyword=" + keyWord + "&pageNo=" + pageNum + "&pageSize=10"));
    }
    public List<Map<String, Object>> getSearchedNewsList(String keyWord, int pageNum, String tag) throws InterruptedException {
        int tagInt = tag2int.get(tag);
        return newsListParser(getPage(baseUrl + "/search?keyword=" + keyWord + "&pageNo=" + pageNum + "&pageSize=10" + "&category=" + tagInt));
    }
    public List<Map<String, Object>> getLatestNewsList(String tag) throws InterruptedException {
        int tagInt = tag2int.get(tag);
        tagPageNum[tagInt] += 1;
        return newsListParser(getPage(baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=10&category=" + tagInt));
    }
    public List<Map<String, Object>> getLatestNewsList() throws InterruptedException{
        int tagInt = 0;
        tagPageNum[tagInt] += 1;
        return newsListParser(getPage(baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=10"));
    }
    public Map<String, Object> getNews(String newsId) throws InterruptedException, JSONException {
        String jsonText = null;
        if(mydb != null){
            if(mydb.exists(newsId)){
                jsonText = (String)mydb.get(newsId).get("com_json");
            }
        }
        if(jsonText == null || jsonText.equals("")){
            jsonText = getPage(baseUrl + "/detail?newsId=" + newsId);
            if(mydb != null){
                mydb.insertCom(jsonText);
            }
        }
        return newsParser(jsonText);
    }
    public Map<String, Object> getNews(Map<String, Object> news) throws InterruptedException, JSONException {
        String jsonText = null;
        String newsId = (String)news.get("news_ID");
        if(mydb != null){
            if(mydb.exists(newsId)){
                jsonText = (String)mydb.get(newsId).get("com_json");
            }
        }
        if(jsonText == null || jsonText.equals("")){
            jsonText = getPage(baseUrl + "/detail?newsId=" + newsId);
            if(mydb != null){
                mydb.insertCom(jsonText);
            }
        }
        return newsParser(jsonText);
    }
    public static Map<String, Object> newsParser(String jsonText) throws JSONException {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObj = new JSONObject(jsonText);
        Iterator<String> keys = jsonObj.keys();
        String key = null;
        String value = null;
        while(keys.hasNext())
        {
            key = keys.next();
            value = jsonObj.getString(key);
            result.put(key, value);
        }
        String[] key2list = {"persons", "locations", "organizations", "Keywords", "bagOfWords"};
        for(int i = 0; i < key2list.length; ++i)
        {
            String keyWord = key2list[i];
            JSONArray listObj = new JSONArray((String)result.get(keyWord));
            List<Map<String, Object>> templist = new ArrayList<Map<String, Object>>();
            for(int j = 0; j < listObj.length(); ++j){
                Map<String, Object> temp = new HashMap<String, Object>();
                JSONObject tempObj = listObj.getJSONObject(j);
                keys = tempObj.keys();
                key = null;
                value = null;
                while(keys.hasNext()){
                    key = keys.next();
                    value = tempObj.getString(key);
                    temp.put(key, value);
                }
                templist.add(temp);
            }
            result.put(keyWord, templist);
        }
        return result;
    }
    public static Map<String, Object> simNewsParser(String jsonText) throws JSONException{
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject newsObj = new JSONObject(jsonText);
        Iterator<String> keys = newsObj.keys();
        String key = null;
        String value = null;
        while(keys.hasNext())
        {
            key = keys.next();
            value = newsObj.getString(key);
            map.put(key, value);
        }
        return map;
    }
    public List<Map<String, Object>> newsListParser(String jsonText) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try{
            JSONObject jsonObj = new JSONObject(jsonText);
            JSONArray listObj = jsonObj.getJSONArray("list");
            for(int i = 0; i < listObj.length(); ++i)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject newsObj = listObj.getJSONObject(i);
                if(mydb != null){
                    try {
                        mydb.insert(newsObj.toString());
                    } catch (Exception e){
                        Log.i(TAG, "newsListParser: ", e);
                    }
                }
                Iterator<String> keys = newsObj.keys();
                String key = null;
                String value = null;
                while(keys.hasNext())
                {
                    key = keys.next();
                    value = newsObj.getString(key);
                    map.put(key, value);
                }
                result.add(map);
            }
        } catch (Exception e)
        {
            Log.i(TAG, "newsParser: ", e);
        }
        return result;
    }
    public String getPage(String targetUrl) throws InterruptedException {
        this.targetUrl = targetUrl;
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        /*try{
            mydb.insert(jsonText);
        } catch (Exception e){
            Log.i(TAG, "getPage: ", e);
            Log.i(TAG, "getPageerror: " + jsonText);
        }*/
        return jsonText;
    }
    Runnable runnable = new Runnable() {
        public static final String TAG = "runnable";

        @Override
        public void run() {
            try{
                URL url = new URL(targetUrl);
                BufferedReader bfr = new BufferedReader(new InputStreamReader(url.openStream()));
                jsonText = bfr.readLine();
            } catch(Exception e)
            {
                Log.i(TAG, "run: ", e);
            }
        }
    };
}
