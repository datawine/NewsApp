package com.example.newsapp.view.briefinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.newsapp.MyApplication;
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
    private SearchView mSearchView;
    private ListView mListView;
    private String selectTitle;
    private String[] category;
    private int len;

    private IBriefPresenter iBriefPresenter;

    private MyApplication app;

    private static BriefInfoActivity instance;

    public static BriefInfoActivity getInstance()
    {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_info);


        instance = this;

        //初始化ui
        iBriefPresenter = new IBriefPresenterCompl(this);


        pgAdapter = new PageListAdapter(getSupportFragmentManager(), this, len, category);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pgAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);

        // 设置搜索文本监听

        mSearchView.setSubmitButtonEnabled(true);

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                app = MyApplication.getInstance();

                app.SetSearchText(query);

                BriefInfoActivity bri = BriefInfoActivity.getInstance();

                bri.onResume();

                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        iBriefPresenter = new IBriefPresenterCompl(this);
        pgAdapter = new PageListAdapter(getSupportFragmentManager(), this, len, category);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pgAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

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