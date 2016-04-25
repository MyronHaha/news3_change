package com.example.whoami.news;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by whoami on 2016/4/20.
 */
public class MyHolder {
    View view;

    MyHolder(View view){
        name= (TextView) view.findViewById(R.id.name);
        time= (TextView) view.findViewById(R.id.time);
        message=(TextView) view.findViewById(R.id.message);
    }
    public TextView name;
    public TextView time;
    public TextView message;
}
