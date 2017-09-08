package com.example.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.newsapp.view.Splash.SplashActivity;

/**
 * Created by junxian on 9/7/2017.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent=new Intent();
        intent.setClass(MainActivity.this, SplashActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}