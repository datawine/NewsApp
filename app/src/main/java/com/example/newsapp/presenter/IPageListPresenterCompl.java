package com.example.newsapp.presenter;

import com.example.newsapp.view.briefinfo.IPageListView;

/**
 * Created by jzp1025 on 17/9/11.
 */

public class IPageListPresenterCompl implements IPageListPresenter {

    private IPageListView iPageListView;

    public IPageListPresenterCompl(IPageListView iPageListView)
    {
        this.iPageListView = iPageListView;
    }

    public void GetInitDatas()
    {
        //从数据库获得个数及名称

        int count = 9;
        String[] news = {"0","1","2","3","4","5","6","7","8"};

        iPageListView.InitDatas(count,news);
    }
}
