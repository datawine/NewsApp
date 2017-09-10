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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newsapp.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.Inflater;

public class ChangeTagActivity extends AppCompatActivity {
    private ArrayList<String> mVals = new ArrayList<String>();
    private HashMap<String, Boolean> mSelected = new HashMap<String, Boolean>();
    private ArrayList<String> mShowVals = new ArrayList<String>();
    private HashMap<String, Boolean> mShowSelected = new HashMap<String, Boolean>();
    private TagAdapter<String> mTagAdapter, mShowTagAdapter;
    private TagFlowLayout mTagFlowLayout, mShowFlowLayout;
    private Button btn_add, btn_delete, btn_clear_show, btn_clear_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tag);

        mShowFlowLayout = (TagFlowLayout) findViewById(R.id.id_cur_flowlayout);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);


        //模拟读入数据
        initData();

        //

        initAdapter();
        mTagFlowLayout.setAdapter(mTagAdapter);
        setSelectListener();

        initShowAdapter();
        mShowFlowLayout.setAdapter(mShowTagAdapter);
        setShowSelectListener();

        btn_add = (Button) findViewById(R.id.btn_add_tag);
        btn_delete = (Button) findViewById(R.id.btn_delete_tag);
        btn_clear_show = (Button) findViewById(R.id.btn_clear_tag_show);
        btn_clear_all = (Button) findViewById(R.id.btn_clear_tag_all);

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
                        for (String key : mShowVals) {
                            if (mShowSelected.get(key)) {
                                Iterator iter = mShowVals.iterator();
                                while (iter.hasNext()) {
                                    if (iter.next().equals(key)) {
                                        iter.remove();
                                    }
                                }
                                mShowSelected.remove(key);
                            }
                            else
                                mShowSelected.put(key, false);
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
    }

    public void initData() {
        mVals.add("测试1");
        mVals.add("测试2");
        mVals.add("测试3");
        mSelected.put("测试1", false);
        mSelected.put("测试2", false);
        mSelected.put("测试3", false);
        mShowVals.add("显示测试1");
        mShowSelected.put("显示测试1", false);
    }

    public void initAdapter() {
        mTagAdapter = new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                RelativeLayout rl = (RelativeLayout) LayoutInflater.from(ChangeTagActivity.this)
                        .inflate(R.layout.tag_textview, mTagFlowLayout, false);

                if (mSelected.get(s))
                    rl.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_dark));
                else
                    rl.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_light));

                TextView tv = (TextView) rl.findViewById(R.id.tag_text);
                tv.setText(s);
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

                if (mShowSelected.get(s))
                    rl.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_dark));
                else
                    rl.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_light));

                TextView tv = (TextView) rl.findViewById(R.id.tag_text);
                tv.setText(s);
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

}