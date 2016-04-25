package com.example.whoami.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by whoami on 2016/4/1.
 */
public class ListAdapter1 extends BaseAdapter{
    private String[] titles={"title-1","title-2","title-3","title-4"};
    private int[] icons = {R.mipmap.home,R.mipmap.home,R.mipmap.home,R.mipmap.home};
    Context context;
    public ListAdapter1(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return titles[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View v = inflater.inflate(R.layout.news_style1, null);
        TextView textview = (TextView)v.findViewById(R.id.message);
        ImageView imageView = (ImageView)v.findViewById(R.id.image);
        textview.setText(titles[i]);;
        imageView.setImageResource(icons[i]);

        return v;
    }
}

