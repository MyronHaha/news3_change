package com.example.whoami.news.slideMenu;

/**
 * Created by Myron on 2016/4/20.
 */
public class News {
    private String title;
    private String detail;
    private Integer comment;
    private String imageUrl;
    private String content;
    public News() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public News(String title, String content, String detail, String imageUrl, Integer comment) {
        this.title = title;
        this.content=content;

        this.detail = detail;
        this.imageUrl = imageUrl;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "news[title="+title+",detail="+detail+",comment="+
                comment+",imageUrl="+imageUrl+"]";
    }
}
