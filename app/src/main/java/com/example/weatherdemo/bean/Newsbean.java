package com.example.weatherdemo.bean;

public class Newsbean {
    String brief_introduction_url;
    String title;
    String content;
    String original_text_url;

    public Newsbean() {
    }

    public Newsbean(String brief_introduction_url, String title, String content, String original_text_url) {
        this.brief_introduction_url = brief_introduction_url;
        this.title = title;
        this.content = content;
        this.original_text_url = original_text_url;
    }

    public String getBrief_introduction_url() {
        return brief_introduction_url;
    }

    public void setBrief_introduction_url(String brief_introduction_url) {
        this.brief_introduction_url = brief_introduction_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOriginal_text_url() {
        return original_text_url;
    }

    public void setOriginal_text_url(String original_text_url) {
        this.original_text_url = original_text_url;
    }
}
