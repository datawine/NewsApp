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
import com.example.newsapp.view.settings.MySettingsActivity;

/**
 * Created by junxian on 9/7/2017.
 */

public class BriefInfoActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PageListAdapter pgAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NavigationView mNavigationView;
    private String selectTitle;
    private String[] category;
    private int len;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_info);

        String[] tmpcat = {"推荐", "历史", "科技", "房产", "613", "地球", "人文", "时尚", "娱乐"};
        getCategory(tmpcat);

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
        int id = item.getItemId();
        item.setChecked(false);

        if (id == R.id.nav_collection) {
            Toast.makeText(getApplicationContext(), id,
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(BriefInfoActivity.this, MySettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), id,
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), id,
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_homepage) {
            viewPager.setCurrentItem(0);
        }

        //关闭侧滑菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    public void goDetail(String mCategory, String mtitle) {
        Intent intent = new Intent(BriefInfoActivity.this, DetailInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Category", mCategory);
        bundle.putString("Title", mtitle);
        intent.putExtras(bundle);

        int forDetailCode = 1000;
        startActivityForResult(intent, forDetailCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewPager.setCurrentItem(category2index(data.getStringExtra("Category")));
    }
*/
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
}