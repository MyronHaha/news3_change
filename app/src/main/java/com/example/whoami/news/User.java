package com.example.whoami.news;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by whoami on 2016/4/3.
 */
public class User {
    private String name;
    private String pwd;
    private String email;
    private Map<String,String> firends;

    public  Map<String,String> getFirends() {
        return firends;
    }

    public void setFirends( Map<String,String> firends) {
        this.firends = firends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "{name:"+name+",emial:"+email+",pwd:"+pwd+",friends:"+firends+"}";
    }
}
