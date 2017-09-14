package com.example.newsapp.presenter;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.example.newsapp.MyApplication;
import com.example.newsapp.view.briefinfo.IPageListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jzp1025 on 17/9/11.
 */

public class    IPageListPresenterCompl extends Activity implements IPageListPresenter {

    private IPageListView iPageListView;
    private MyApplication app;

    private List<Map<String, Object>> list;

    public IPageListPresenterCompl(IPageListView iPageListView)
    {
        this.iPageListView = iPageListView;
    }

    public void GetInitDatas(String cat)
    {
        //从数据库获得个数及名称

        app = MyApplication.getInstance();

        ArrayList<Map<String, Object>> simplenews = new ArrayList<Map<String, Object>>();
        int count = 0;


        if(cat == "收藏夹")
        {

            list = app.GetStarNews();

            for (int i = 0; i < list.size(); i++) {
                count++;

                simplenews.add(list.get(i));

            }
        }
        else {

            if(cat == "搜索")
            {try {
                list = app.GetSearchNewsList(app.GetQuery());
            } catch (InterruptedException e) {
            }

            try {
                for (int i = 0; i < list.size(); i++) {
                    count++;

                    simplenews.add(list.get(i));

                }
            } catch (Exception e){
                Log.i("aaa", "GetInitDatas: ", e);
            }

            }
            else {
                if(cat == "推荐")
                {
                    try {
                        list = app.GetLatestNewsList(app.GetTagPage(cat));
                    } catch (InterruptedException e) {
                    }

                    for (int i = 0; i < list.size(); i++) {
                        count++;

                        simplenews.add(list.get(i));

                    }

                }
                else {
                    try {
                        list = app.GetLatestNewsList(cat);
                    } catch (InterruptedException e) {
                    }

                    for (int i = 0; i < list.size(); i++) {
                        count++;

                        simplenews.add(list.get(i));

                    }
                }
            }
        }
        iPageListView.InitDatas(count,simplenews);
    }
}
