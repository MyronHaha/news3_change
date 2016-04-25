package com.example.whoami.news.slideMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.whoami.news.R;
import com.loopj.android.image.SmartImageView;

/**
 * Created by Myron on 2016/4/20.
 */
public class news_detail extends Activity{
    TextView title,detail;
    SmartImageView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.news_detail);
        super.onCreate(savedInstanceState);
       title= (TextView) findViewById(R.id.d_title);
        detail= (TextView) findViewById(R.id.d_detail);
        sv= (SmartImageView) findViewById(R.id.image);
        Intent d_intent=getIntent();

        title.setText(d_intent.getStringExtra("title"));
        detail.setText(d_intent.getStringExtra("detail"));
        sv.setImageUrl(d_intent.getStringExtra("imageUrl"));

    }
}