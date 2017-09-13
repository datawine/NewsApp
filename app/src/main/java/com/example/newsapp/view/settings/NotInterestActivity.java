package com.example.newsapp.view.settings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.NoInterestAdapter;

import java.util.ArrayList;

public class NotInterestActivity extends AppCompatActivity {
    private ArrayList<String> mBanVal;
    private NoInterestAdapter mAdapter;
    private ListView mListView;
    private ImageButton mAddBtn;
    private String addcontent;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_interest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化数据
        mBanVal = new ArrayList<String>();
        mBanVal.add("测试1");
        mBanVal.add("测试2");
        mBanVal.add("测试3");
        //

        mAddBtn = (ImageButton) findViewById(R.id.btn_nointerest_add);
        mListView = (ListView) findViewById(R.id.listView_nointerest);
        mEditText = (EditText) findViewById(R.id.edit_nointerest);
        mAdapter = new NoInterestAdapter(NotInterestActivity.this, mBanVal);

        mListView.setAdapter(mAdapter);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcontent = mEditText.getText().toString();

                //添加屏蔽关键词
                mBanVal.add(addcontent);
                mAdapter.notifyDataSetChanged();

                //
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Snackbar.make(view, "删除！" + mBanVal.get(position), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });

    }
}
