package com.example.weatherdemo.bean;

public class USSTNews {
    String title;
    String url;
    String content;

    public USSTNews() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public USSTNews(String title, String url, String content) {
        this.title = title;
        this.url = url;
        this.content = content;
    }
}
