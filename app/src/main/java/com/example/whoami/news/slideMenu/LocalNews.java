package com.example.whoami.news.slideMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whoami.news.MainActivity;
import com.example.whoami.news.R;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myron on 2016/4/20.
 */
public class LocalNews extends Activity{
    TextView tx;
    ListView lv;
    String xmlString;
    List<News> nList=new ArrayList<News>();
    private  final int SUCCEEDED=0;
    private final int FAILED=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case SUCCEEDED:
                   nList= (List<News>) msg.obj;
                   Log.v("handler获取到的list",nList.get(3).getImageUrl().toString());
                   MyAdapter localnews_adapter=new MyAdapter();
                   lv.setAdapter(localnews_adapter);
                   break;
               case FAILED:
                   Toast.makeText(LocalNews.this,"网络崩溃了",Toast.LENGTH_SHORT).show();
                   break;
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.localnews);
        super.onCreate(savedInstanceState);
        tx= (TextView) findViewById(R.id.local_tx);
        lv= (ListView) findViewById(R.id.lv_localnews);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News onenew=nList.get(i);
                Intent intent =new Intent(LocalNews.this,news_detail.class);
                intent.putExtra("title",onenew.getTitle());
                intent.putExtra("detail",onenew.getDetail());
                intent.putExtra("imageUrl",onenew.getImageUrl());
                startActivity(intent);
            }
        });
        tx.setText((this.getIntent().getBundleExtra("menu_bundle")).getString("msg"));
        httpUtils httpUtils =new httpUtils ("http://1.xungehello.applinzi.com/news/news.xml",handler);
        httpUtils.start();

    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return nList.size();
        }

        @Override
        public Object getItem(int i) {
            return nList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view=convertView;
            ViewHolder viewholder;
            if(view==null){

                LayoutInflater inflater=getLayoutInflater();
                view=inflater.inflate(R.layout.localnews_item,null);
                viewholder=new ViewHolder();
               viewholder.image= (SmartImageView) view.findViewById(R.id.my_image);
                viewholder.title= (TextView) view.findViewById(R.id.item_title);
               viewholder.content = (TextView) view.findViewById(R.id.item_content);
                viewholder.comment= (TextView) view.findViewById(R.id.item_comment);
                view.setTag(viewholder);
            }else{
                viewholder= (ViewHolder) convertView.getTag();
            }

            News oneNew =nList.get(position);
            viewholder.image.setImageUrl(oneNew.getImageUrl());
            viewholder.title.setText(oneNew.getTitle());
            viewholder.content.setText(oneNew.getContent());
            viewholder.comment.setText(oneNew.getComment()+"评论");
            return view;
        }
         class ViewHolder{
            private TextView title;
            private TextView content;
            private TextView comment;
            private SmartImageView image;
        }
    }
}
