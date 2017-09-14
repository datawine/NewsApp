package com.java.team_33.presenter;

import android.view.MenuItem;

import com.java.team_33.view.detailinfo.DetailInfoActivity;

import org.json.JSONException;

/**
 * Created by jzp1025 on 17/9/11.
 */

public interface IDetailPresenter {

    public void CheckItemId(MenuItem item, DetailInfoActivity ac);

    public void GetTitle(String ID) throws InterruptedException, JSONException;

    public void GetContent(String ID) throws InterruptedException, JSONException;

    public void GetKeyWords(String ID) throws JSONException, InterruptedException;

    public void GetVoice(String ID);
}
