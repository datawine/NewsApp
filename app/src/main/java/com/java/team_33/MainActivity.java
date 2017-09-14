package com.java.team_33;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.java.team_33.view.mysplash.SplashActivity;

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