package com.example.newsapp.view.briefinfo;

import android.content.*;

/**
 * Created by jzp1025 on 17/9/11.
 */

public interface IBriefView {

    public void getCategory(String[] cat);

    public int category2index(String category);

    public void SetViewPager();

    public void GetActivityStart(Intent intent);

    public void GetToast(int id);
}
