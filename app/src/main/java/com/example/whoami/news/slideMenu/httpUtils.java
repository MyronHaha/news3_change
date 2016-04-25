package com.example.whoami.news.slideMenu;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import io.shaded.socket.client.Url;

/**
 * Created by Myron on 2016/4/24.
 */
public class httpUtils extends Thread{
    String sr_url;
    Handler handler;
    String xmlString;
    List<News> nList=null;
    private  final int SUCCEEDED=0;
    private final int FAILED=1;
    public httpUtils(String url,Handler handler){
        this.sr_url=url;
        this.handler=handler;
    }
    @Override
    public void run() {


        nList=getlistFromNet(sr_url);
        Message msg=new Message();
        if(nList!=null){
            msg.what=SUCCEEDED;
            msg.obj=nList;
        }else{
            msg.what=FAILED;
        }
        handler.sendMessage(msg);
    }
    public List<News> getlistFromNet(String url){

        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn= (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream is;
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                 is=conn.getInputStream();
                 nList=xml_parse(is);
            }else{

                Log.v("connect_error","网络错误");
            }
        }catch (Exception ex){
            System.out.println("error:"+ex.toString());
        }

        return nList;
    }

    private List<News> xml_parse(InputStream is) throws Exception{

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is,"utf-8");
        int eventype =parser.getEventType();
        List<News> list=null;
        News anew=null;
        while(eventype!=XmlPullParser.END_DOCUMENT){  //</news>
            String tagName=parser.getName();  //节点名称；
            switch (eventype){
                case XmlPullParser.START_TAG:
                    if(tagName.equals("news")){
                        list=new ArrayList<News>();
                    }else if(tagName.equals("new")){
                        anew=new News();

                    }else if(tagName.equals("title")){
                        anew.setTitle(parser.nextText());

                    }else if(tagName.equals("content")) {
                        anew.setContent(parser.nextText());
                    }else if(tagName.equals("detail")) {
                        anew.setDetail(parser.nextText());
                    }else if(tagName.equals("comment")) {
                        anew.setComment(Integer.valueOf(parser.nextText()));
                    }else if(tagName.equals("image")) {
                        anew.setImageUrl(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(tagName.equals("new")){
                        list.add(anew);
                    }
                    break;
                default:break;
            }
                 eventype=parser.next(); //取下一个事件类型；
        }
        Log.v("list",list.toString());
     return list;
    }


}
