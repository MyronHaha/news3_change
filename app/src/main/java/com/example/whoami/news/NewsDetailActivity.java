package com.example.whoami.news;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whoaim.dao.News;
import com.wilddog.client.Wilddog;

public class NewsDetailActivity extends AppCompatActivity {

    ScrollView scrollView;
    LinearLayout ll = null;
    ListView pinglun;
    ImageDownLoader mImageDownLoader;
    private String key;
    Wilddog ref;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        myinit();
        initIntent();//初始化news对象
        initPinglun();

    }

    private void myinit() {
        scrollView= (ScrollView) this.findViewById(R.id.scrollView);
        ll = (LinearLayout) this.findViewById(R.id.ll);
    }

    private void initIntent(){
        Intent intent =  getIntent();
        mImageDownLoader = new ImageDownLoader(this);
        News new_item = intent.getParcelableExtra("new_item");
        key = intent.getStringExtra("key");
        Toast.makeText(NewsDetailActivity.this, key, Toast.LENGTH_SHORT).show();
        if(new_item!=null){

            initNews(new_item);
        }else{
            Toast.makeText(this, "抱歉没有该新闻", Toast.LENGTH_SHORT).show();
        }
    }
    private void initTool() {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initPinglun() {
        Fragment pinglunFragment = new Fragment();
        View v = getLayoutInflater().inflate(R.layout.writepinglun,null);

        pinglun = new ListView(this);
        ll.addView(pinglun);
        pinglun.addFooterView(v,null,true);
        //ll.addView();
        initData();

    }


    private void initData() {
        ref = new Wilddog(MyConst.PATH);
    }


    private void initNews(News n){
        int length = n.getMessage().length();
        String msg = n.getMessage();
        StringBuffer sb = new StringBuffer();
        int imagepos = 0;

        for(int i = 0 ; i<length ;i++){
            char word = msg.charAt(i);
            if (word!='★'){
                sb.append(word);
            }else{

                String txt = sb.toString();
                sb.delete(0, sb.length());
                if(!txt.equals("")) {

                    TextView tv = new TextView(this);

                    tv.setTextSize(40);
                    tv.setText(txt+"\r\n");

                    ll.addView(tv);
                }
                ImageView iv = new ImageView(this);

                Bitmap b = mImageDownLoader.downloadImage(MyConst.KEY2,MyConst.STO + n.getImage()[imagepos],200 ,200,new ImageSet(iv));
                if(b!=null) {
                    iv.setImageBitmap(b);
                }else{
                    iv.setImageResource(R.mipmap.noimage);
                }
                ll.addView(iv);

                imagepos++;
            }
        }
        TextView auth = new TextView(this);
        auth.setText(n.getAuth());
        auth.setTextColor(Color.BLUE);
        ll.addView(auth);
        initPinglun();
    }

    class ImageSet implements ImageDownLoader.onImageLoaderListener{
        ImageView iv;
        public ImageSet(ImageView iv){
            this.iv = iv;
        }
        @Override
        public void onImageLoader(Bitmap bitmap, String url) {

            if (iv==null){
                Toast.makeText(NewsDetailActivity.this, "imageview is null", Toast.LENGTH_SHORT).show();
            }
            iv.setImageBitmap(bitmap);//空指针

        }
    }
}
