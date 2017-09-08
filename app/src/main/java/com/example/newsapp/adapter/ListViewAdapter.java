package com.example.newsapp.adapter;

/**
 * Created by junxian on 9/8/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.singleitem.SingleListItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<SingleListItem> listDatas;
    private ItemViewHolder viewHolder;

    public ListViewAdapter(Context context, ArrayList<SingleListItem> listDatas) {
        this.listDatas = listDatas;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //返回你有多少个不同的布局
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SingleListItem listItem = listDatas.get(position);
        String type = listItem.type;

        if (convertView == null) {
            viewHolder = new ItemViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.info_item, null);
        }

        viewHolder.content = (TextView) convertView.findViewById(R.id.text_item);
        viewHolder.content.setText(listItem.map.get("Content").toString());
        viewHolder.content.setBackgroundColor(Color.parseColor(type2Color(type)));

        return convertView;
    }

    public String type2Color(String type) {
        if (type == "normal") {
            return "#7fa87f";
        }
        else {
            return "#00ff00";
        }
    }

    class ItemViewHolder {
        TextView content;
    }

}