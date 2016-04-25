package com.example.whoaim.dao;

/**
 * Created by whoami on 2016/4/18.
 */
public class PingLun {
    private String me;
    private String time;
    private String message;
    private String to;
    private int good;//点赞
    //ServerValue.TIMESTAMP
    public PingLun(){}
    public PingLun(String me, String time, String message, String to, int good) {
        this.me = me;
        this.time = time;
        this.message = message;
        this.to = to;
        this.good = good;
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    @Override
    public String toString() {
        return "PingLun{" +
                "me='" + me + '\'' +
                ", time='" + time + '\'' +
                ", message='" + message + '\'' +
                ", to='" + to + '\'' +
                ", good=" + good +
                '}';
    }
}

