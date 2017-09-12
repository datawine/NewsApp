package com.example.newsapp.presenter;

import android.view.MenuItem;

import com.example.newsapp.view.detailinfo.DetailInfoActivity;

import org.json.JSONException;

/**
 * Created by jzp1025 on 17/9/11.
 */

public interface IDetailPresenter {

    public void CheckItemId(MenuItem item, DetailInfoActivity ac);

    public void GetTitle(String ID) throws InterruptedException, JSONException;

    public void GetContent(String ID) throws InterruptedException, JSONException;
}
