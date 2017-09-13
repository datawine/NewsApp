package com.example.newsapp.view.settings;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.BoringLayout;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newsapp.MyApplication;
import com.example.newsapp.R;
import com.example.newsapp.view.briefinfo.BriefInfoActivity;
import com.example.newsapp.view.briefinfo.PageListFragment;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.Inflater;

import com.example.newsapp.presenter.*;
import com.example.newsapp.view.briefinfo.PageListFragment;

public class ChangeTagActivity extends AppCompatActivity implements IChangeView{
    private ArrayList<String> mVals = new ArrayList<String>();
    private HashMap<String, Boolean> mSelected = new HashMap<String, Boolean>();
    private ArrayList<String> mShowVals = new ArrayList<String>();
    private HashMap<String, Boolean> mShowSelected = new HashMap<String, Boolean>();
    private TagAdapter<String> mTagAdapter, mShowTagAdapter;
    private TagFlowLayout mTagFlowLayout, mShowFlowLayout;
    private ImageButton btn_add, btn_delete, btn_clear_show, btn_clear_all;

    private IChangePresenter iChangePresenter;

    private MyApplication app;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tag);

        iChangePresenter = new IChangePresenterCompl(this);

        mShowFlowLayout = (TagFlowLayout) findViewById(R.id.id_cur_flowlayout);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);


        //模拟读入数据
        iChangePresenter.GetInitData();

        //

        initAdapter();
        mTagFlowLayout.setAdapter(mTagAdapter);
        setSelectListener();

        initShowAdapter();
        mShowFlowLayout.setAdapter(mShowTagAdapter);
        setShowSelectListener();

        btn_add = (ImageButton) findViewById(R.id.btn_add_tag);
        btn_delete = (ImageButton) findViewById(R.id.btn_delete_tag);
        btn_clear_show = (ImageButton) findViewById(R.id.btn_clear_tag_show);
        btn_clear_all = (ImageButton) findViewById(R.id.btn_clear_tag_all);

        app = MyApplication.getInstance();

        //增加
        btn_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Set<String> hs = mSelected.keySet();
                        Set<String> showSet = mShowSelected.keySet();

                        for (String key : hs) {
                            if (!showSet.contains(key) && mSelected.get(key)) {
                                mShowVals.add(key);
                                mShowSelected.put(key, false);

                                app.AddTag(key);
                            }
                            mSelected.put(key, false);
                        }
                        mTagAdapter.notifyDataChanged();
                        mShowTagAdapter.notifyDataChanged();
                    }
                }
        );

        //删除
        btn_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> tmpval = new ArrayList<String>();
                        Collections.addAll(tmpval, new String[mShowVals.size()]);
                        Collections.copy(tmpval, mShowVals);
                        mShowVals.clear();
                        for (String key : tmpval) {
                            if (mShowSelected.get(key)) {
                                mShowSelected.remove(key);

                                app.DelTag(key);

                            }
                            else {
                                mShowVals.add(key);
                            }
                        }
                        mTagAdapter.notifyDataChanged();
                        mShowTagAdapter.notifyDataChanged();
                    }
                }
        );

        btn_clear_all.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Set<String> hs = mSelected.keySet();
                        for (String key : hs) {
                            mSelected.put(key, false);
                        }
                        mTagAdapter.notifyDataChanged();
                    }
                }
        );

        btn_clear_show.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Set<String> hs = mShowSelected.keySet();
                        for (String key : hs) {
                            mShowSelected.put(key, false);
                        }
                        mShowTagAdapter.notifyDataChanged();
                    }
                }
        );


        //刷新主界面


    }

    public void SetVals(String[] vals)
    {
        for(int i=0;i<vals.length;i++)
        {
            mVals.add(vals[i]);

            mSelected.put(vals[i], false);
        }
    }

    public void SetShowVal(String[] showval)
    {
        for(int i=0;i<showval.length;i++)
        {
            mShowVals.add(showval[i]);

            mShowSelected.put(showval[i], false);
        }

    }


    public void initAdapter() {
        mTagAdapter = new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                RelativeLayout rl = (RelativeLayout) LayoutInflater.from(ChangeTagActivity.this)
                        .inflate(R.layout.tag_textview, mTagFlowLayout, false);

                TextView tv = (TextView) rl.findViewById(R.id.tag_text);
                if (mSelected.get(s))
                    tv.setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    tv.setBackgroundColor(getResources().getColor(R.color.primary));

                tv.setText(s);
                tv.setTextColor(getResources().getColor(R.color.icons));
                return rl;
            }
        };
    }

    public void setSelectListener() {
        mTagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                for (int i : selectPosSet) {
                    mSelected.put(mVals.get(i), true);
                }
                mTagAdapter.notifyDataChanged();
            }
        });
    }


    public void initShowAdapter() {
        mShowTagAdapter = new TagAdapter<String>(mShowVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                RelativeLayout rl = (RelativeLayout) LayoutInflater.from(ChangeTagActivity.this)
                        .inflate(R.layout.tag_textview, mShowFlowLayout, false);

                TextView tv = (TextView) rl.findViewById(R.id.tag_text);
                if (mShowSelected.get(s))
                    tv.setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    tv.setBackgroundColor(getResources().getColor(R.color.primary));

                tv.setText(s);
                tv.setTextColor(getResources().getColor(R.color.icons));
                return rl;
            }
        };
    }

    public void setShowSelectListener() {
        mShowFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                for (int i : selectPosSet) {
                    mShowSelected.put(mShowVals.get(i), true);
                }
                mShowTagAdapter.notifyDataChanged();
            }
        });
    }

    @Override
    public void finish()
    {
        BriefInfoActivity bri = BriefInfoActivity.getInstance();

        bri.onResume();

        super.finish();
    }

}