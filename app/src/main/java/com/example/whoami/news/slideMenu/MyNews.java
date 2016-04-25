package com.example.whoami.news.slideMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.whoami.news.R;

/**
 * Created by Myron on 2016/4/20.
 */
public class MyNews extends Activity{
    TextView tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.mynews);
        super.onCreate(savedInstanceState);
        tx = (TextView) findViewById(R.id.mynews_tx);
        tx.setText((this.getIntent().getBundleExtra("menu_bundle")).getString("msg"));
        Intent intent =new Intent(MyNews.this,TestWrite.class);
        startActivity(intent);
    }
}
