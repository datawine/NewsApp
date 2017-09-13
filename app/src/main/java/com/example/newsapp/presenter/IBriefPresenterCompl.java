package com.example.newsapp.presenter;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newsapp.MyApplication;
import com.example.newsapp.R;
import com.example.newsapp.adapter.PageListAdapter;
import com.example.newsapp.view.briefinfo.*;
import com.example.newsapp.view.settings.ChangeTagActivity;
import com.example.newsapp.view.settings.MySettingsActivity;
import com.example.newsapp.view.settings.NotInterestActivity;


/**
 * Created by jzp1025 on 17/9/11.
 */

public class IBriefPresenterCompl implements IBriefPresenter {

    private IBriefView iBriefView;
    private MyApplication app;


    public IBriefPresenterCompl(IBriefView iBriefView) {


        this.iBriefView = iBriefView;

    //初始化数据
    // 这里加载当前类别  ****从model层获得

        app = MyApplication.getInstance();



        String[] tmpcat;
        tmpcat = app.GetTags();

        iBriefView.getCategory(tmpcat);


}

    public void CheckItemId(MenuItem item,BriefInfoActivity ac)
    {
        int id = item.getItemId();
        item.setChecked(false);

        if (id == R.id.nav_collection) {
            Intent intent = new Intent(ac, ChangeTagActivity.class);
            iBriefView.GetActivityStart(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ac, MySettingsActivity.class);
            iBriefView.GetActivityStart(intent);
        } else if (id == R.id.nav_share) {
            iBriefView.GetToast(id);

        } else if (id == R.id.nav_send) {
            iBriefView.GetToast(id);

        } else if (id == R.id.nav_homepage) {
            iBriefView.SetViewPager();
        } else if (id == R.id.nav_ban) {
            Intent intent = new Intent(ac, NotInterestActivity.class);
            iBriefView.GetActivityStart(intent);
        }

    }
}
