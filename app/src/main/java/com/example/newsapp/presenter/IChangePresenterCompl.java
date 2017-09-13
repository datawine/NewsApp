package com.example.newsapp.presenter;

import com.example.newsapp.MyApplication;
import com.example.newsapp.view.settings.IChangeView;

/**
 * Created by jzp1025 on 17/9/11.
 */

public class IChangePresenterCompl implements IChangePresenter{

    private IChangeView iChangeView;
    private MyApplication app;

    public IChangePresenterCompl(IChangeView iChangeView)
    {
        this.iChangeView = iChangeView;


    }

    public void GetInitData()
    {

        app = MyApplication.getInstance();

        String[] vals ={"科技","教育","军事","国内","社会", "文化", "汽车","国际","体育","财经","健康","娱乐"};

        String[] showval = app.GetTags();

        iChangeView.SetVals(vals);

        iChangeView.SetShowVal(showval);

    }
}
