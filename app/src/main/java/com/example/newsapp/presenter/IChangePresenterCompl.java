package com.example.newsapp.presenter;

import com.example.newsapp.view.settings.IChangeView;

/**
 * Created by jzp1025 on 17/9/11.
 */

public class IChangePresenterCompl implements IChangePresenter{

    private IChangeView iChangeView;

    public IChangePresenterCompl(IChangeView iChangeView)
    {
        this.iChangeView = iChangeView;


    }

    public void GetInitData()
    {

        String[] vals = {"测试1","测试2","测试3"};

        String showval = "显示测试";

        iChangeView.SetVals(vals);

        iChangeView.SetShowVal(showval);

    }
}
