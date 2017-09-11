package com.example.newsapp.model;

import android.util.Log;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by leavan on 2017/9/11.
 */
public class MySqliteTest {
    private static final String TAG = "MySqliteTest";

    @Test
    public void initTest() throws Exception{
        /*File file = new File("/data/data/com.example.newsapp/");
        if(file.exists()){
            Log.i(TAG, "initTest: " + "YES");
            String[] strs = file.list();
            for(int i = 0; i < strs.length; ++i)
            {
                Log.i(TAG, "initTest: " + strs[i]);
            }
        } else{
            Log.i(TAG, "initTest: " + "NO");
        }
        Log.i(TAG, "initTest: " + "aaaaaaaaa");
        */
        MySqlite mySqlite = new MySqlite();
        mySqlite.init();
        mySqlite.insert();
        Log.i(TAG, "initTest: " + mySqlite.query());
    }
}