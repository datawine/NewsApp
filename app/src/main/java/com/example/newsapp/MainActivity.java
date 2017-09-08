package com.example.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.newsapp.view.BriefInfoActivity;

/**
 * Created by junxian on 9/7/2017.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent=new Intent();
        //setClass函数的第一个参数是一个Context对象
        //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
        //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
        intent.setClass(MainActivity.this, BriefInfoActivity.class);
        startActivity(intent);
    }
}