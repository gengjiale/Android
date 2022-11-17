package com.example.weatherdemo.bean;

public class StudentNews {
    String url;
    String title;
    String context;

    public StudentNews() {
    }

    public StudentNews(String url, String title, String context) {
        this.url = url;
        this.title = title;
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
