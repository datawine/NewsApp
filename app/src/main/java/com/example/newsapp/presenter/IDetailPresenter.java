package com.example.newsapp.presenter;

import android.view.MenuItem;

import com.example.newsapp.view.detailinfo.DetailInfoActivity;

/**
 * Created by jzp1025 on 17/9/11.
 */

public interface IDetailPresenter {

    public void CheckItemId(MenuItem item, DetailInfoActivity ac);

    public void GetTitle();

    public void GetContent();
}
