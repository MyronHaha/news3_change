package com.example.whoaim.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by whoami on 2016/4/2.
 */
public class News implements Parcelable {

    String auth; //作者
    String title; //标题
    String message;//新闻内容
    int time;//发表时间
    String[] image;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }




    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    //序列化实体类
    public static final Parcelable.Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel parcel) {
            News news = new News();

            news.message = parcel.readString();
            news.auth= parcel.readString();
            news.title = parcel.readString();
            news.type = parcel.readString();
            news.time = parcel.readInt();
           // parcel.readStringArray(news.image);
            int length = parcel.readInt();
            news.image = new String[length];
            parcel.readStringArray(news.image);
            return news;
        }



        @Override
        public News[] newArray(int i) {
            return new News[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(auth);
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeInt(time);
        //parcel.writeStringArray(image);
        if(image!=null){
            parcel.writeInt(image.length);
        }else{
            parcel.writeInt(0);
        }
        parcel.writeStringArray(image);

    }

    @Override
    public String toString() {
        return "News{" +
                "auth='" + auth + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", image=" + Arrays.toString(image) +
                ", type='" + type + '\'' +
                '}';
    }
}
