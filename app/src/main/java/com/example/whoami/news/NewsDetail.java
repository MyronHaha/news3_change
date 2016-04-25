package com.example.whoami.news;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whoaim.dao.News;
import com.example.whoaim.dao.PingLun;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import java.util.ArrayList;
import java.util.Iterator;

public class NewsDetail extends Activity implements WritePinlunTool.ChangeView {
    private ListView listview;
    private String key;
    private News new_item;
    private ScrollView scrollView;
    private LinearLayout news;

    private ImageDownLoader mImageDownLoader;
    Wilddog ref = null;

    FragmentManager fragmentManager;
    FragmentTransaction mytransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.news_detial);
        initSql();
        initData();
        initFoot();
        getData();

    }

    private void initSql() {
        mImageDownLoader = new ImageDownLoader(this);
        ref = new Wilddog(MyConst.PATH);
    }
    WritePinlunTool writePinlunTool = null;
    WritePinlunToolChange writePinlunToolChange = null;
    private void initFoot() {
        fragmentManager = getFragmentManager();
        mytransaction = fragmentManager.beginTransaction();
        writePinlunTool = new WritePinlunTool();

        writePinlunToolChange= new WritePinlunToolChange(key);
        mytransaction.add(R.id.foot, writePinlunTool);
        mytransaction.add(R.id.foot, writePinlunToolChange);
        mytransaction.show(writePinlunTool).hide(writePinlunToolChange);
        mytransaction.addToBackStack(null);
        mytransaction.commit();

        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

/*
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width, height);
        param.setMargins(0, height - 150, 0, 0);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.foot);
        frameLayout.setLayoutParams(param);
*/
        LinearLayout freshfoot = (LinearLayout) getLayoutInflater().inflate(R.layout.footfresh, null,false);
        listview.addFooterView(freshfoot);
    }


    private void initData() {
        listview = (ListView) this.findViewById(R.id.pinglun);

        new_item = getNewsAndKey();
        scrollView = (ScrollView) this.findViewById(R.id.scrollView);
        news = (LinearLayout) findViewById(R.id.news);
        initNews(new_item);
    }
//未完成 newpinglunitem
    private void wiretePlinglun(PingLun pingLun){
        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.newpinglunitem,null);
        TextView name = (TextView) item.findViewById(R.id.name);
        TextView message = (TextView)item.findViewById(R.id.message);
        TextView time = (TextView) item.findViewById(R.id.time);
        name.setText(pingLun.getMe());
        message.setText(pingLun.getMessage());
        time.setText(pingLun.getTime());

        ref.child(MyConst.PINGLUN).child(key).push().setValue(pingLun);
    }
    public News getNewsAndKey() {
        Intent intent = getIntent();
        News new_item = intent.getParcelableExtra("new_item");
        key = intent.getStringExtra("key");
        Toast.makeText(NewsDetail.this, key + new_item.toString(), Toast.LENGTH_SHORT).show();
        return new_item;
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

                    tv.setTextSize(20);
                    tv.setText(txt+"\r\n");

                    news.addView(tv);
                }
                ImageView iv = new ImageView(this);

                Bitmap b = mImageDownLoader.downloadImage("84948",MyConst.STO + n.getImage()[imagepos],MyConst.NEWS_WIDTH ,MyConst.NEWS_HEIGHT,new ImageSet(iv));
                if(b!=null) {
                    iv.setImageBitmap(b);
                }else{
                    iv.setImageResource(R.mipmap.noimage);
                }
                news.addView(iv);

                imagepos++;
            }
        }
        TextView auth = new TextView(this);
        auth.setText(n.getAuth());
        auth.setTextColor(Color.BLUE);
        news.addView(auth);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(NewsDetail.this, ""+fragmentManager.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
            if(fragmentManager.getBackStackEntryCount()==1){
                finish();
            }else{
                fragmentManager.popBackStack();
                count=0;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private static int count = 0;
    @Override
    public void change() {
        mytransaction = fragmentManager.beginTransaction();
        Toast.makeText(NewsDetail.this, ""+count, Toast.LENGTH_SHORT).show();

        mytransaction.hide(writePinlunTool).show(writePinlunToolChange);
        //writePinlunTool.loss();
        if(count==0) {
            Toast.makeText(NewsDetail.this, "add stack", Toast.LENGTH_SHORT).show();
            mytransaction.addToBackStack("key");
            count++;
        }
        //mytransaction.commitAllowingStateLoss();
        mytransaction.commit();
    }

    class ImageSet implements ImageDownLoader.onImageLoaderListener{
        ImageView iv;
        public ImageSet(ImageView iv){
            this.iv = iv;
        }
        @Override
        public void onImageLoader(Bitmap bitmap, String url) {

            if (iv==null){
                Toast.makeText(NewsDetail.this, "imageview is null", Toast.LENGTH_SHORT).show();
            }
            iv.setImageBitmap(bitmap);//空指针

        }
    }
    public void getData() {
        ref.child(MyConst.PINGLUN).child(key).limit(5).addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<PingLun> pls = new ArrayList<PingLun>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot ds = it.next();
                    PingLun pingLun= (PingLun) ds.getValue(PingLun.class);
                    pls.add(pingLun);
                }
                if(pls.size()>0){
                    MyAdapter myAdapter = new MyAdapter(pls,NewsDetail.this);
                    listview.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {

            }
        });
    }
    class MyAdapter extends BaseAdapter{
        private ArrayList<PingLun> pls = null;
        private Context context;
       MyAdapter(ArrayList<PingLun> pls,Context context){
           this.pls = pls;
           this.context=context;//异步的同步思考
       }
        @Override
        public int getCount() {
            return pls.size();
        }

        @Override
        public Object getItem(int i) {
            return pls.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyHolder holder = null;
            View desview;
            if(view==null) {
                desview = getLayoutInflater().inflate(R.layout.newpinglunitem,null);
                holder = new MyHolder(desview);
                desview.setTag(holder);
            }else{
                desview = view;
                holder = (MyHolder) desview.getTag();
            }
            PingLun pingLun = pls.get(i);

            holder.name.setText(pingLun.getMe());
            holder.message.setText(pingLun.getMessage());
            holder.time.setText(pingLun.getTime());

            return desview;

        }

    }
}