package com.example.newsapp.view.briefinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adapter.PageListAdapter;
import com.example.newsapp.view.settings.ChangeTagActivity;
import com.example.newsapp.view.settings.MySettingsActivity;

import com.example.newsapp.presenter.*;

/**
 * Created by junxian on 9/7/2017.
 */

public class BriefInfoActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,IBriefView{

    private PageListAdapter pgAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NavigationView mNavigationView;
    private String selectTitle;
    private String[] category;
    private int len;

    private IBriefPresenter iBriefPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_info);


        //初始化ui
        iBriefPresenter = new IBriefPresenterCompl(this);


        pgAdapter = new PageListAdapter(getSupportFragmentManager(), this, len, category);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pgAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        iBriefPresenter.CheckItemId(item,this);

        //关闭侧滑菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void getCategory(String[] cat) {
        category = cat;
        len = category.length;
    }

    public int category2index(String category) {
        for (int i = 0; i < len; i ++) {
            if (this.category[i] == category) {
                return i;
            }
        }
        return 0;
    }

    public void SetViewPager()
    {

        viewPager.setCurrentItem(0);
    }

    public void GetActivityStart(Intent intent)
    {
        startActivity(intent);
    }

    public void GetToast(int id)
    {
        Toast.makeText(getApplicationContext(), id,Toast.LENGTH_SHORT).show();
    }
}