package com.example.newsapp.view.briefinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.newsapp.MyApplication;
import com.example.newsapp.R;
import com.example.newsapp.adapter.ListViewAdapter;
import com.example.newsapp.presenter.IChangePresenter;
import com.example.newsapp.presenter.IPageListPresenter;
import com.example.newsapp.singleitem.SingleListItem;
import com.example.newsapp.view.detailinfo.DetailInfoActivity;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.newsapp.presenter.*;

import org.json.JSONException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;

/**
 * Created by junxian on 9/7/2017.
 */

public class PageListFragment extends Fragment implements IPageListView{
    public static final String CATEGORY = "CATEGORY";
    private String mCategory;
    private ArrayList<SingleListItem> mListItems;
    private PullToRefreshListView mPullRefreshListView;
    private ListViewAdapter mAdapter;

    HashMap<String, Object> map;

    private IPageListPresenter iPageListPresenter;

    private List<String> banList;

    private static PageListFragment instance;

    public static PageListFragment newInstance(String category) {
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        PageListFragment pageListFragment = new PageListFragment();
        pageListFragment.setArguments(args);
        return pageListFragment;
    }

    public static PageListFragment getInstance() {

        return instance;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getString(CATEGORY);

        iPageListPresenter = new IPageListPresenterCompl(this);

        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_page_list, container, false);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);

        iPageListPresenter.GetInitDatas(mCategory);


        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        // 设置监听事件
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
                {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        String label = DateUtils.formatDateTime(
                                getActivity().getApplicationContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // 显示最后更新的时间
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        if (mPullRefreshListView.isHeaderShown()) {

                            MyApplication app = MyApplication.getInstance();

                            app.SetHeaderorFooter(false);

                            // 模拟加载任务

                            final GetDataTask dataTask = new GetDataTask();
                            dataTask.execute();



                        }
                        else if (mPullRefreshListView.isFooterShown()){
                            MyApplication app = MyApplication.getInstance();

                            app.SetHeaderorFooter(true);

                            new GetDataTask().execute();

                        }

                    }
                });

        //设置点击事件
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("Category", mCategory);
                bundle.putString("Title", mListItems.get(position - 1).map.get("Content").toString());
                bundle.putString("ID",mListItems.get(position - 1).map.get("ID").toString());
                bundle.putString("Author", mListItems.get(position - 1).map.get("Author").toString());
                bundle.putString("Time", mListItems.get(position - 1).map.get("Time").toString());
                intent.putExtras(bundle);

                startActivity(intent);

                //                            int forDetailCode = 1000;
                //                            startActivityForResult(intent, forDetailCode);
            }
        });

        return view;
    }

    public void InitDatas(int count , ArrayList<Map<String, Object>> simplenews) {
        // 初始化数据和数据源
        mListItems = new ArrayList<SingleListItem>();

        MyApplication app;



        for (int i = 0; i < count; i++)
         if(CheckBanNews((String)simplenews.get(i).get("news_ID")))
        {
            map = new HashMap<String, Object>();
            map.put("Title", simplenews.get(i).get("news_Title"));
            map.put("Author", simplenews.get(i).get("news_Author"));
            map.put("Time", simplenews.get(i).get("news_Time"));
            map.put("Content", simplenews.get(i).get("news_Title")+"\n"+simplenews.get(i).get("news_Author")+"\n"+simplenews.get(i).get("news_Time"));
            map.put("ID",simplenews.get(i).get("news_ID"));

            app = MyApplication.getInstance();
            map.put("IsRead",app.IsRead((String)simplenews.get(i).get("news_ID")));

            if (app.GetPic((String)simplenews.get(i).get("news_ID"))!=null)
                map.put("Pic",app.GetPic((String)simplenews.get(i).get("news_ID")));


            // || simplenews.get(i).get("getPicture")==null;
            if (!app.getPicMode() || (app.GetPic((String)simplenews.get(i).get("news_ID"))==null))
                mListItems.add(new SingleListItem("normal", map));
            else
                mListItems.add(new SingleListItem("pic", map));
        }
        mAdapter = new ListViewAdapter(getActivity(), mListItems);
        mPullRefreshListView.setAdapter(mAdapter);
    }

    private boolean CheckBanNews(String ID) {




        MyApplication app = MyApplication.getInstance();

        String title ="";
        try {
            title = (String) app.GetNews(ID).get("news_Title");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        banList = app.GetBanList();


        for(int i=0;i<banList.size();i++)
            if(title.indexOf(banList.get(i))!= -1)
                return false;

        return true;


    }

    private class GetDataTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params)
        {
            MyApplication app = MyApplication.getInstance();

            if(mCategory != "推荐" && mCategory != "收藏夹") {
                app.AddTagPage(mCategory);

                app.SetNewList(mCategory);
            }
            else
            {
                if(mCategory == "推荐")
                {
                    app.SetNewList(mCategory);
                }
            }

            return "Done";

        }

        @Override
        protected void onPostExecute(String result)
        {

            MyApplication app = MyApplication.getInstance();

            boolean flag = app.GetFlag();

            if(mCategory != "推荐" && mCategory != "收藏夹" && flag) {
                app = MyApplication.getInstance();

                List<Map<String, Object>> mapList = app.GetNewList();

                for (int i = 0; i < mapList.size(); i++)
                    if(CheckBanNews((String)mapList.get(i).get("news_ID")))
                {

                    map = new HashMap<String, Object>();
                    map.put("Title", mapList.get(i).get("news_Title"));
                    map.put("Author", mapList.get(i).get("news_Author"));
                    map.put("Time", mapList.get(i).get("news_Time"));
                    map.put("Content", mapList.get(i).get("news_Title") + "\n" + mapList.get(i).get("news_Author") + "\n" + mapList.get(i).get("news_Time"));
                    map.put("ID", mapList.get(i).get("news_ID"));

                    app = MyApplication.getInstance();
                    map.put("IsRead",app.IsRead((String)mapList.get(i).get("news_ID")));

                    if (app.GetPic((String)mapList.get(i).get("news_ID"))!=null)
                        map.put("Pic",app.GetPic((String)mapList.get(i).get("news_ID")));


                    // || simplenews.get(i).get("getPicture")==null;
                    if (!app.getPicMode() || (app.GetPic((String)mapList.get(i).get("news_ID"))==null))
                        mListItems.add(new SingleListItem("normal", map));
                    else
                        mListItems.add(new SingleListItem("pic", map));

                }
            }
            else
            {
                if(mCategory != "收藏夹" && !flag )
                {
                    mListItems = new ArrayList<SingleListItem>();

                    app = MyApplication.getInstance();

                    List<Map<String, Object>> mapList = app.GetNewList();

                    for (int i = 0; i < mapList.size(); i++)
                        if(CheckBanNews((String)mapList.get(i).get("news_ID")))
                    {

                        map = new HashMap<String, Object>();
                        map.put("Title", mapList.get(i).get("news_Title"));
                        map.put("Author", mapList.get(i).get("news_Author"));
                        map.put("Time", mapList.get(i).get("news_Time"));
                        map.put("Content", mapList.get(i).get("news_Title") + "\n" + mapList.get(i).get("news_Author") + "\n" + mapList.get(i).get("news_Time"));
                        map.put("ID", mapList.get(i).get("news_ID"));

                        app = MyApplication.getInstance();
                        map.put("IsRead",app.IsRead((String)mapList.get(i).get("news_ID")));

                        if (app.GetPic((String)mapList.get(i).get("news_ID"))!=null)
                            map.put("Pic",app.GetPic((String)mapList.get(i).get("news_ID")));


                        // || simplenews.get(i).get("getPicture")==null;
                        if (!app.getPicMode() || (app.GetPic((String)mapList.get(i).get("news_ID"))==null))
                            mListItems.add(new SingleListItem("normal", map));
                        else
                            mListItems.add(new SingleListItem("pic", map));

                    }
                }
            }

                mAdapter = new ListViewAdapter(getActivity(), mListItems);

                mAdapter.notifyDataSetChanged();
                // Call onRefreshComplete when the list has been refreshed.
                //mPullRefreshListView.onRefreshComplete();

                mPullRefreshListView.onRefreshComplete();


        }
    }

}

