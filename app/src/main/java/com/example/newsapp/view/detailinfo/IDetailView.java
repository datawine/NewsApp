package com.example.newsapp.view.detailinfo;

import android.content.Intent;

import java.util.List;

/**
 * Created by jzp1025 on 17/9/11.
 */

public interface IDetailView {


    public void GetActivityStart(Intent intent);

    public void GetToast(int id);

    public void GetFinished();

    public void SetTitle(String title);

    public void SetContent(String content);

    public void SetKeyWords(List<String> keyWords);

}
