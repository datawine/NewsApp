package com.example.newsapp.presenter;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.view.detailinfo.DetailInfoActivity;
import com.example.newsapp.view.detailinfo.DetailInfoActivity;
import com.example.newsapp.view.detailinfo.IDetailView;
import com.example.newsapp.view.settings.ChangeTagActivity;
import com.example.newsapp.view.settings.MySettingsActivity;

/**
 * Created by jzp1025 on 17/9/11.
 */

public class IDetailPresenterCompl implements IDetailPresenter {

    private IDetailView iDetailView;

    public IDetailPresenterCompl(IDetailView iDetailView)
    {
        this.iDetailView = iDetailView;


    }

    public void CheckItemId(MenuItem item, DetailInfoActivity ac)
    {

        int id = item.getItemId();

        if (id == R.id.nav_collection) {
            iDetailView.GetToast(id);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ac, MySettingsActivity.class);
            iDetailView.GetActivityStart(intent);
        } else if (id == R.id.nav_share) {
            iDetailView.GetToast(id);
        } else if (id == R.id.nav_send) {
            iDetailView.GetToast(id);
        } else if (id == R.id.nav_homepage) {
            iDetailView.GetFinished();
        }

    }

    public void GetTitle()
    {
        String title = "title";//从数据库获取title

        iDetailView.SetTitle(title);
    }

    public void GetContent()
    {
        String content = "this is content.";//从数据库获取content

        iDetailView.SetContent(content);
    }
}
