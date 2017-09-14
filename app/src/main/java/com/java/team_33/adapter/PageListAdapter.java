package com.java.team_33.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.java.team_33.view.briefinfo.PageListFragment;

/**
 * Created by junxian on 9/7/2017.
 */

public class PageListAdapter extends FragmentPagerAdapter {
    int PAGE_COUNT;
    private String tabTitles[];
    private Context context;

    public PageListAdapter(FragmentManager fm, Context context, int page_count, String[] ss) {
        super(fm);
        this.PAGE_COUNT = page_count;
        this.tabTitles = ss;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageListFragment.newInstance(tabTitles[position]);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
