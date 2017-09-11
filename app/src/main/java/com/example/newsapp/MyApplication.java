package com.example.newsapp;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;

/**
 * Created by junxian on 9/9/2017.
 */

public class MyApplication extends Application {
    private boolean dayMode;
    UiModeManager mUiModeManager;

    @Override
    public void onCreate() {
        super.onCreate();
        setDayMode(true);
        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
    }

    public boolean getDayMode() {
        return dayMode;
    }

    public void setDayMode(boolean mode) {
        dayMode = mode;
    }
}
