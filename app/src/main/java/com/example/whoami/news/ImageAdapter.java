package com.example.whoami.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whoaim.dao.News;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 这个类给新闻浏览提供数据或图片
 */

public class ImageAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    Context context;
    Wilddog ref ;
    ArrayList<News> news;
    ArrayList<String> key;
    ImageDownLoader imageDownLoader;
    private Activity activity;
    public ImageAdapter(Context context){
        this.activity = (Activity) context;
        this.context=context;
        Wilddog.setAndroidContext(context);
        imageDownLoader = new ImageDownLoader(context);
        ref= new Wilddog(MyConst.PATH);
        initData();
    }

    /**
     * 初始化后端的数据
     */
    private void initData() {

        news= new ArrayList<>();
        key = new ArrayList<>();

        ref.child("news").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    DataSnapshot ds = it.next();
                    key.add(ds.getKey());
                    News news_item = (News) ds.getValue(News.class);
                    news.add(news_item);//从数据库下载的新闻数据
                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {

            }
        });

    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        News new_item = news.get(i);

        View v=GetViewItem(new_item,view);
        return v;
    }

    private View GetViewItem(News new_item,View view) {
        View v = null;
        switch (getType(new_item)){
            case 0:
                v = initViewFor1(new_item,view);
                break;
            case 1:
                v = initViewFor2(new_item,view);
                break;
            case 3:
                v = initViewFor3(new_item,view);
                break;
        }
        return  v;
    }
    private void setImage(String imageurl, final ImageView imageview) {
        Bitmap img = imageDownLoader.downloadImage(MyConst.KEY1, imageurl, MyConst.NEWS_WIDTH, MyConst.NEWS_HEIGHT, new ImageDownLoader.onImageLoaderListener() {

            @Override
            public void onImageLoader(Bitmap bitmap, String url) {
                if (bitmap != null) {
                    imageview.setImageBitmap(bitmap);
                } else {
                    imageview.setImageResource(R.mipmap.noimage);
                }
            }
        });
        if (img != null) {
            imageview.setImageBitmap(img);
        } else {
            imageview.setImageResource(R.mipmap.noimage);
        }
    }


    private View initViewFor1(News new_item,View view) {
        View descView = view.inflate(context,R.layout.news_style0,null);
        TextView title = (TextView) descView.findViewById(R.id.title);
        TextView auth = (TextView) descView.findViewById(R.id.auth);
        TextView message = (TextView) descView.findViewById(R.id.message);
        title.setText(new_item.getTitle());
        auth.setText(new_item.getAuth());
        message.setText(new_item.getMessage());

        return descView;
    }
    private View initViewFor2(News new_item,View view) {
        //return   initViewFor1(new_item,view);
        View descView = view.inflate(context,R.layout.news_style1,null);
        TextView title = (TextView) descView.findViewById(R.id.title);
        TextView auth = (TextView) descView.findViewById(R.id.auth);
        title.setText(new_item.getTitle());
        auth.setText(new_item.getAuth());

        final ImageView imageview = (ImageView) descView.findViewById(R.id.image);
        String imageurl = MyConst.STO + new_item.getImage()[0];
        setImage(imageurl, imageview);

        return descView;
    }



    private View initViewFor3(News new_item,View view) {

        View descView = view.inflate(context,R.layout.news_style3,null);
        TextView title = (TextView) descView.findViewById(R.id.title);
        TextView auth = (TextView) descView.findViewById(R.id.auth);
        ImageView firstImage = (ImageView) descView.findViewById(R.id.img1);
        ImageView secondImage = (ImageView) descView.findViewById(R.id.img2);
        ImageView threeImage = (ImageView) descView.findViewById(R.id.img3);
        title.setText(new_item.getTitle());
        auth.setText(new_item.getAuth());
        String imageurl1 = MyConst.STO+new_item.getImage()[0];
        String imageurl2 = MyConst.STO+ new_item.getImage()[1];
        String imageurl3 =  MyConst.STO+new_item.getImage()[2];

        setImage(imageurl1,firstImage);
        setImage(imageurl2,secondImage);
        setImage(imageurl3,threeImage);

        return descView;
    }

    private int getType(News news) {
        String type = news.getType();
        if(type.equals("0")){
            return 0;
        }else if(type.equals("1"))
        {
            return 1;
        }else if(type.equals("2"))
        {
            return 2;
        }else if(type.equals("3")){
            return 3;
        }
        return 0;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(context,news.get(i).toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context,NewsDetail.class);
        News new_item =news.get(i);
        intent.putExtra("key",key.get(i));
        intent.putExtra("new_item",new_item);
        activity.startActivity(intent);
    }
}