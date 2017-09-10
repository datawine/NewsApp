package com.example.newsapp.view.settings;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.Set;
import java.util.zip.Inflater;

public class ChangeTagActivity extends AppCompatActivity {
    private String[] mVals = {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello"};
    private boolean[] mSelected = {false, false, false, false, false, false};
    private String[] mShowVals = {};
    private boolean[] mShowSelected = {};
    private TagAdapter<String> mTagAdapter, mShowTagAdapter;
    private TagFlowLayout mTagFlowLayout, mShowFlowLayout;
    private Button btn_add, btn_delete, btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tag);

        mShowFlowLayout = (TagFlowLayout) findViewById(R.id.id_cur_flowlayout);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
/*
        initAdapter(mTagFlowLayout, mTagAdapter, mVals, mSelected);
        mTagFlowLayout.setAdapter(mTagAdapter);
        setSelectListener(mTagFlowLayout, mTagAdapter, mSelected);

        initAdapter(mShowFlowLayout, mShowTagAdapter, mShowVals, mSelected);
        mShowFlowLayout.setAdapter(mTagAdapter);
        setSelectListener(mShowFlowLayout, mTagAdapter, mShowSelected);
*/

        initAdapter();
        mTagFlowLayout.setAdapter(mTagAdapter);
        setSelectListener();

        btn_add = (Button) findViewById(R.id.btn_add_tag);
        btn_delete = (Button) findViewById(R.id.btn_delete_tag);
        btn_clear = (Button) findViewById(R.id.btn_delete_tag);

        //增加的逻辑没有写去重
        btn_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );

        btn_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );

        btn_clear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );
    }

    public void initData(String[] str) {
        mVals = str;
    }

    public void initAdapter() {
        mTagAdapter = new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                RelativeLayout rl = (RelativeLayout) LayoutInflater.from(ChangeTagActivity.this)
                        .inflate(R.layout.tag_textview, mTagFlowLayout, false);

                if (mSelected[position])
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
                    mSelected[i] = true;
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

                if (mShowSelected[position])
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
                    mShowSelected[i] = true;
                }
                mShowTagAdapter.notifyDataChanged();
            }
        });
    }

}