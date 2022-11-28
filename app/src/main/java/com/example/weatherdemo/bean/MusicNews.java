package com.example.weatherdemo.bean;

public class MusicNews {
    String song_name;
    String singer_name;
    String song_link;

    public MusicNews() {
    }

    public MusicNews(String song_name, String singer_name, String song_link) {
        this.song_name = song_name;
        this.singer_name = singer_name;
        this.song_link = song_link;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public String getSong_link() {
        return song_link;
    }

    public void setSong_link(String song_link) {
        this.song_link = song_link;
    }
}
