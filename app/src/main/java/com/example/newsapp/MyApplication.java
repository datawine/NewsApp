package com.example.newsapp;

import android.app.Application;

/**
 * Created by junxian on 9/9/2017.
 */

public class MyApplication extends Application {
    private boolean dayMode;

    @Override
    public void onCreate() {
        super.onCreate();
        setDayMode(true);
    }

    public boolean getDayMode() {
        return dayMode;
    }

    public void setDayMode(boolean mode) {
        dayMode = mode;
    }
}
