package com.example.newsapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.newsapp.adapter.PageListAdapter;
import com.example.newsapp.view.PageListFragment;

/**
 * Created by junxian on 9/7/2017.
 */

public class BriefInfoActivity extends FragmentActivity {

    private PageListAdapter pgAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_info);

        String[] category = {"推荐", "历史", "科技", "房产", "613", "地球", "人文", "时尚", "娱乐"};
        int len = category.length;

        pgAdapter = new PageListAdapter(getSupportFragmentManager(), this, len, category);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pgAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}