package com.example.newsapp.view.settings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld" };
    private ArrayList arrTab;
    private TagAdapter<String> mTagAdapter;
    private TagFlowLayout mTagFlowLayout;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tag);

        arrTab = new ArrayList();
        //添加一条数据用于添加标签的替换
        arrTab.add("tab");

        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        mTagFlowLayout.setAdapter(new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
//                TextView tv = (TextView) LayoutInflater.from(ChangeTagActivity.this)
//                       .inflate(R.layout.tag_textview, mTagFlowLayout, false);

                RelativeLayout rl = (RelativeLayout) LayoutInflater.from(ChangeTagActivity.this)
                        .inflate(R.layout.tag_textview, mTagFlowLayout, false);
                Log.i("finish", "finish");
                TextView tv = (TextView) rl.findViewById(R.id.tag_text);
                tv.setText(s);
                return rl;
            }
        });

        mTagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                Log.i("choose:" , " choose"+ selectPosSet.toString());
            }
        });
    }
}