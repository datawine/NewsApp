package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.newsapp.R;

import java.util.ArrayList;

/**
 * Created by junxian on 9/13/2017.
 */

public class NoInterestAdapter extends BaseAdapter {
    private ArrayList<String> data;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private TextView tv;

    public NoInterestAdapter(Context context, ArrayList<String> data) {
        //传入data
        this.context = context;
        this.data = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = mLayoutInflater.inflate(R.layout.nointerest_item, null);
        tv = (TextView) convertView.findViewById(R.id.nointerest_textitem);

        tv.setText(data.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
