package com.example.newsapp.adapter;

/**
 * Created by junxian on 9/8/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.singleitem.SingleListItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<SingleListItem> listDatas;
    private ItemViewHolder viewHolder;
    private boolean mIsRead;

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
            if (type == "normal") {
                convertView = mLayoutInflater.inflate(R.layout.info_item, null);
            } else if (type == "pic" && listItem.map.get("Pic").toString() != null){
                convertView = mLayoutInflater.inflate(R.layout.info_item_with_image, null);
                Log.i("image", listItem.map.get("Pic").toString());
            }
        }

        viewHolder.ll = (LinearLayout) convertView.findViewById(R.id.info_item_layout);
        if ((boolean) listItem.map.get("IsRead")) {
            viewHolder.ll.setBackgroundColor(viewHolder.ll.getResources().getColor(R.color.readbackground));
        } else {
            viewHolder.ll.setBackgroundColor(viewHolder.ll.getResources().getColor(R.color.background));
        }


        viewHolder.Title = (TextView) convertView.findViewById(R.id.brief_title);
        viewHolder.Title.setText(listItem.map.get("Title").toString());
        viewHolder.Title.setTextColor(viewHolder.Title.getResources().getColor(R.color.primary_text));

        viewHolder.Author = (TextView) convertView.findViewById(R.id.brief_author);
        viewHolder.Author.setText(listItem.map.get("Author").toString());
        viewHolder.Author.setTextColor(viewHolder.Author.getResources().getColor(R.color.secondary_text));

        viewHolder.Time = (TextView) convertView.findViewById(R.id.brief_time);
        viewHolder.Time.setText(listItem.map.get("Time").toString());
        viewHolder.Time.setTextColor(viewHolder.Time.getResources().getColor(R.color.secondary_text));

        if (type == "pic"  && listItem.map.get("Pic").toString() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(listItem.map.get("Pic").toString());

            if(bitmap == null){
                return convertView;
            }
            float w = bitmap.getWidth(); // 得到图片的宽，高
            float h = bitmap.getHeight();

            float centerw = w / 2;
            float centery = h / 3;
            float cropx, cropy;
            float towidth, toheight;
            float scalex, scaley;

            if (w / h > (float) 340 / 210) {
                towidth = h / 210 * 340;
                toheight = h;
            } else {
                toheight = w / 340 * 210;
                towidth = w;
            }

            cropx = centerw - towidth / 2;
            cropy = centery - toheight / 2;
            if (cropy < 0)
                cropy = 0;

            scalex = 340 / towidth;
            scaley = 210 / toheight;

            Matrix matrix = new Matrix();
            matrix.postScale(scalex, scaley);
            Bitmap bmp = Bitmap.createBitmap(bitmap, (int)cropx, (int)cropy, (int)towidth, (int)toheight, matrix, false);
            if(bmp == null){
                return convertView;
            }
            viewHolder.Image = (ImageView) convertView.findViewById(R.id.detail_info_image);
            if(viewHolder.Image == null){
                return convertView;
            }
            viewHolder.Image.setImageBitmap(bmp);
        }

        return convertView;
    }

    class ItemViewHolder {
        LinearLayout ll;
        TextView Title;
        TextView Time;
        TextView Author;
        ImageView Image;
    }

}