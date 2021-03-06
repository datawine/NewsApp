package com.java.team_33.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by leavan on 2017/9/10.
 */

public class NewsManager {
    private static final String TAG = "newsParser";
    private static final String basePath = "/data/data/com.example.newsapp/";
    private static final HashMap<String, Integer> tag2int = new HashMap<String, Integer>(){{
        put("推荐", 0);put("科技", 1);put("教育", 2);put("军事", 3);
        put("国内", 4);put("社会", 5);put("文化", 6);put("汽车", 7);
        put("国际", 8);put("体育", 9);put("财经", 10);put("健康", 11);
        put("娱乐", 12);
    }};
    private int[] tagPageNum = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final String baseUrl = "http://166.111.68.66:2042/news/action/query";
    //private static String goodUrl = "";
    //private static String goodJson = "";
    private String targetUrl = null;
    private String jsonText = null;
    Map<String, String> oldListJson = new HashMap<String, String>();
    MySqlite mydb = null;

    public NewsManager(MySqlite db){
        this.mydb = db;
        for(int i = 1; i <= 12; ++i){
            try {
                newsListParserWithoutBlock(getPage(baseUrl + "/latest?pageNo=" + 1 + "&pageSize=100&category=" + i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public NewsManager(){

    }

   /* public List<Map<String, Object>> __getSearchedNewsList(String keyWord, int pageNum) throws InterruptedException {
        String url = baseUrl + "/search?keyword=" + keyWord + "&pageNo=" + pageNum + "&pageSize=100";
        List<Map<String, Object>> result;
        if(url.equals(goodUrl)) {
            result = newsListParser(goodJson);
        } else {
            result = newsListParser(getPage(baseUrl + "/search?keyword=" + keyWord + "&pageNo=" + pageNum + "&pageSize=100"));
        }
        goodUrl = baseUrl + "/search?keyword=" + keyWord + "&pageNo=" + pageNum + "&pageSize=100";
        goodJson = getPage(goodUrl);
        newsListParserWithoutBlock(goodJson);
        return result;
    }*/
    public List<Map<String, Object>> getSearchedNewsList(String keyWord, int pageNum) throws InterruptedException {
        String url = baseUrl + "/search?keyword=" + keyWord + "&pageNo=" + pageNum + "&pageSize=20";
        String jsonText;
        if(oldListJson.containsKey(url)){
            jsonText = oldListJson.get(url);
        } else {
            jsonText = getPage(url);
            oldListJson.put(url, jsonText);
        }
        if(jsonText == null){
            if(pageNum <= 1 || keyWord.equals("")){
                return null;
            }
            //Random random = new Random();
            //return getSearchedNewsList(keyWord, random.nextInt(pageNum - 1) + 1);
            //return getSearchedNewsList(keyWord, pageNum - 1);
        }
        List<Map<String, Object>> result = newsListParserWithoutBlock(jsonText);
        Log.i(TAG, "getSearchedNewsList: " + jsonText);
        url = baseUrl + "/search?keyword=" + keyWord + "&pageNo=" + (pageNum + 1) + "&pageSize=20";
        jsonText = getPage(url);
        oldListJson.put(url, jsonText);
        newsListParserWithoutBlock(jsonText);
        Log.i(TAG, "getSearchedNewsList: " + "success");
        Random random = new Random();
        if(result.size() < 1){
            if(pageNum == 1){
                return null;
            }
            return getSearchedNewsList(keyWord, random.nextInt(pageNum - 1) + 1);
        }
        return result;
    }
    public List<Map<String, Object>> getLatestNewsList(String tag) throws InterruptedException {
        List<Map<String, Object>> result;
        int tagInt = tag2int.get(tag);
        tagPageNum[tagInt] += 1;
        String url = baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100&category=" + tagInt;
        String jsonText;
        if(oldListJson.containsKey(url)){
            jsonText = oldListJson.get(url);
        } else {
            jsonText = getPage(url);
            oldListJson.put(url, jsonText);
        }
        result = newsListParser(jsonText);
        if(jsonText == null){
            tagPageNum[tagInt] = 1;
            jsonText = getPage(baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100&category=" + tagInt);
            result = newsListParser(jsonText);
        }
        url = baseUrl + "/latest?pageNo=" + (tagPageNum[tagInt] + 1) + "&pageSize=100&category=" + tagInt;
        jsonText = getPage(url);
        oldListJson.put(url, jsonText);
        newsListParserWithoutBlock(jsonText);
        return result;
    }
    public List<Map<String, Object>> getLatestNewsList(String tag, int PageNum) throws InterruptedException {
        List<Map<String, Object>> result;
        int tagInt = tag2int.get(tag);
        String url = baseUrl + "/latest?pageNo=" + PageNum + "&pageSize=100&category=" + tagInt;
        String jsonText;
        if(oldListJson.containsKey(url)){
            jsonText = oldListJson.get(url);
        } else {
            jsonText = getPage(url);
            oldListJson.put(url, jsonText);
        }
        result = newsListParser(jsonText);
        if(jsonText == null){
            jsonText = getPage(baseUrl + "/latest?pageNo=" + (PageNum - 1) + "&pageSize=100&category=" + tagInt);
            result = newsListParser(jsonText);
        }
        url = baseUrl + "/latest?pageNo=" + (PageNum + 1) + "&pageSize=100&category=" + tagInt;
        jsonText = getPage(url);
        oldListJson.put(url, jsonText);
        newsListParserWithoutBlock(jsonText);
        return result;
    }
    /*public List<Map<String, Object>> __getLatestNewsList(String tag) throws InterruptedException {
        List<Map<String, Object>> result;
        int tagInt = tag2int.get(tag);
        tagPageNum[tagInt] += 1;
        String url = baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100&category=" + tagInt;
        String jsonText = null;
        if(url.equals(goodUrl)){
            jsonText = goodJson;
            result = newsListParserWithoutBlock(goodJson);
        } else {
            jsonText = getPage(url);
            result = newsListParser(jsonText);
        }
        if(jsonText == null){
            tagPageNum[tagInt] = 1;
            jsonText = getPage(baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100&category=" + tagInt);
            result = newsListParser(jsonText);
        }
        goodUrl = baseUrl + "/latest?pageNo=" + (tagPageNum[tagInt] + 1) + "&pageSize=100&category=" + tagInt;
        goodJson = getPage(goodUrl);
        newsListParserWithoutBlock(goodJson);
        return result;
    }
    public List<Map<String, Object>> __getLatestNewsList() throws InterruptedException{
        List<Map<String, Object>> result;
        int tagInt = 0;
        tagPageNum[tagInt] += 1;
        String url = baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100";
        Log.i(TAG, "getLatestNewsList: !!!" + url);
        String jsonText = null;
        if(url.equals(goodUrl)){
            jsonText = goodJson;
            result = newsListParserWithoutBlock(goodJson);
        } else {
            jsonText = getPage(url);
            result = newsListParser(jsonText);
        }
        Log.i(TAG, "getLatestNewsList: !!!" + jsonText);
        if(jsonText == null){
            tagPageNum[tagInt] = 1;
            jsonText = getPage(baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100");
            result = newsListParser(jsonText);
        }
        goodUrl = baseUrl + "/latest?pageNo=" + (tagPageNum[tagInt] + 1) + "&pageSize=100";
        goodJson = getPage(goodUrl);
        newsListParserWithoutBlock(goodJson);
        Log.i(TAG, "getLatestNewsList: !!!" + "success");
        return result;
    }*/
    public List<Map<String, Object>> getLatestNewsList() throws InterruptedException {
        List<Map<String, Object>> result;
        int tagInt = 0;
        tagPageNum[tagInt] += 1;
        String url = baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100";
        String jsonText;
        if(oldListJson.containsKey(url)){
            jsonText = oldListJson.get(url);
        } else {
            jsonText = getPage(url);
            oldListJson.put(url, jsonText);
        }
        result = newsListParser(jsonText);
        if(jsonText == null){
            tagPageNum[tagInt] = 1;
            jsonText = getPage(baseUrl + "/latest?pageNo=" + tagPageNum[tagInt] + "&pageSize=100");
            result = newsListParser(jsonText);
        }
        url = baseUrl + "/latest?pageNo=" + (tagPageNum[tagInt] + 1) + "&pageSize=100";
        jsonText = getPage(url);
        oldListJson.put(url, jsonText);
        newsListParserWithoutBlock(jsonText);
        return result;
    }
    public List<Map<String, Object>> getLatestNewsList(int pageNum) throws InterruptedException {
        List<Map<String, Object>> result;
        int tagInt = 0;
        String url = baseUrl + "/latest?pageNo=" + pageNum + "&pageSize=100";
        String jsonText;
        if(oldListJson.containsKey(url)){
            jsonText = oldListJson.get(url);
        } else {
            jsonText = getPage(url);
            oldListJson.put(url, jsonText);
        }
        result = newsListParser(jsonText);
        if(jsonText == null){
            jsonText = getPage(baseUrl + "/latest?pageNo=" + (pageNum - 1) + "&pageSize=100");
            result = newsListParser(jsonText);
        }
        url = baseUrl + "/latest?pageNo=" + (pageNum + 1) + "&pageSize=100";
        jsonText = getPage(url);
        oldListJson.put(url, jsonText);
        newsListParserWithoutBlock(jsonText);
        return result;
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
        String newsId = (String)news.get("news_ID");
        return getNews(newsId);
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
        String pic_String = (String)result.get("news_Pictures");
        String[] pics_String = pic_String.split(" ");
        result.put("pic_Num", new Integer(pics_String.length));
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
        String pic_String = (String)map.get("news_Pictures");
        String[] pics_String = pic_String.split(" ");
        map.put("pic_Num", new Integer(pics_String.length));
        return map;
    }
    public List<Map<String, Object>> newsListParser(String jsonText) throws InterruptedException {
        List<Thread> downloadThreads = new ArrayList<Thread>();
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
                String pic_String = (String)map.get("news_Pictures");
                String[] pics_String = pic_String.split("[ ;]");
                if(pic_String.equals("") || pics_String.length < 1)
                    map.put("pic_Num", new Integer(0));
                else {
                    map.put("pic_Num", new Integer(pics_String.length));
                    if(pics_String.length > 0){
                        try {
                            String picFileName = basePath + (String) map.get("news_ID") + pics_String[0].substring(pics_String[0].lastIndexOf("."));
                            downloadThreads.add(getPictureThread(pics_String[0], picFileName));
                        } catch (Exception e){
                            Log.i(TAG, "newsListParser: ", e);
                            Log.i(TAG, "newsListParser: " + pic_String);
                        }
                    }
                }
                result.add(map);
                if(result.size() >= 10)
                    break;
            }
        } catch (Exception e)
        {
            Log.i(TAG, "newsParser: ", e);
        }
        for(int i = 0; i < downloadThreads.size(); ++i){
            downloadThreads.get(i).join();
        }
        return result;
    }public List<Map<String, Object>> newsListParserWithoutBlock(String jsonText) throws InterruptedException {
        List<Thread> downloadThreads = new ArrayList<Thread>();
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
                String pic_String = (String)map.get("news_Pictures");
                String[] pics_String = pic_String.split("[ ;]");
                if(pic_String.equals("") || pics_String.length < 1)
                    map.put("pic_Num", new Integer(0));
                else {
                    map.put("pic_Num", new Integer(pics_String.length));
                    if(pics_String.length > 0){
                        try {
                            String picFileName = basePath + (String) map.get("news_ID") + pics_String[0].substring(pics_String[0].lastIndexOf("."));
                            downloadThreads.add(getPictureThread(pics_String[0], picFileName));
                        } catch (Exception e){
                            Log.i(TAG, "newsListParser: ", e);
                            Log.i(TAG, "newsListParser: " + pic_String);
                        }
                    }
                }
                result.add(map);
                if(result.size() >= 10){
                    break;
                }
            }
        } catch (Exception e)
        {
            Log.i(TAG, "newsParser: ", e);
        }
        return result;
    }
    public List<Map<String, Object>> newsListParserWithoutDownload(String jsonText) throws InterruptedException {
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
                String pic_String = (String)map.get("news_Pictures");
                String[] pics_String = pic_String.split("[ ;]");
                if(pic_String.equals("") || pics_String.length < 1)
                    map.put("pic_Num", new Integer(0));
                else {
                    map.put("pic_Num", new Integer(pics_String.length));
                    if(pics_String.length > 0){
                        try {
                        } catch (Exception e){
                            Log.i(TAG, "newsListParser: ", e);
                            Log.i(TAG, "newsListParser: " + pic_String);
                        }
                    }
                }
                result.add(map);
                if(result.size() >= 10){
                    break;
                }
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
    static class DownloadThread implements Runnable{
        String tarUrl = "";
        String tarFile = "";
        public void setTask(String tarUrl, String tarFile){
            this.tarUrl = tarUrl;
            this.tarFile = tarFile;
        }
        public void run(){
            if(tarUrl.equals("") || tarFile.equals("")){
                return;
            }
            File file = new File(tarFile);
            if(file.exists()){
                return;
            }
            try {
                URL url = new URL(tarUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                DataInputStream in = new DataInputStream(connection.getInputStream());
                DataOutputStream out = new DataOutputStream(new FileOutputStream(tarFile));
                byte[] buffer = new byte[4096];
                int count = 0;
                while((count = in.read(buffer)) > 0){
                    out.write(buffer, 0, count);
                }
                out.close();
                in.close();
            } catch (Exception e){
                Log.i(TAG, "runError: ", e);
            }
        }
    }
    public static Thread getPictureThread(String picUrl, String fileName){
        DownloadThread dt = new DownloadThread();
        dt.setTask(picUrl, fileName);
        Thread thread = new Thread(dt);
        thread.start();
        return thread;
    }
}
