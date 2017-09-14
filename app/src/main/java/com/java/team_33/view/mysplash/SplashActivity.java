package com.java.team_33.view.mysplash;

import android.os.Bundle;

import com.java.team_33.R;
import com.java.team_33.view.briefinfo.BriefInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 3000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        BriefInfoActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
