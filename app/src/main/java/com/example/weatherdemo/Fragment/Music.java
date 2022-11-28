package com.example.weatherdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherdemo.R;
import com.example.weatherdemo.Weatherdemo;
import com.example.weatherdemo.bean.MusicNews;
import com.example.weatherdemo.bean.StudentNews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import httpunit.Httpunit;

public class Music extends Fragment {
    static List<MusicNews> musicNews = new ArrayList<>();
    private static ListView lv3;
    private static ArrayAdapter adapter;
    Intent i;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music,container,false);
        lv3 = view.findViewById(R.id.Music);
        GetMusicNews music = new GetMusicNews("getMusicNews?str=许嵩");
        music.execute();
        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            MusicNews item = new MusicNews();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = musicNews.get(position);
                String song_name = item.getSong_name();
                String song_singer= item.getSinger_name();
                String base=song_name+"-"+song_singer+".mp3";
                String url = "http://139.196.91.214:8081/music/"+base;
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
//上面的第一个参数为"另一xml"名称
        return view;
    }
    public static class GetMusicNews extends AsyncTask<Void, Void, List<MusicNews>> {
        String newsname="";
        GetMusicNews(String str) {
            newsname= str;
        }
        GetMusicNews(){

        }
        @Override
        protected List<MusicNews> doInBackground(Void... voids) {
            Httpunit httpunit = new Httpunit();
            try {
                musicNews = httpunit.get_MusicNews(newsname);
                return musicNews;
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }
        }
        @Override
        protected void onPostExecute(List<MusicNews> s) {
            super.onPostExecute(s);
            String[] data = new String[1000];
            int i =0;
            for(MusicNews news: musicNews){
                data[i++] = news.getSong_name();
            }
            adapter=new ArrayAdapter(Weatherdemo.getInstance(),android.R.layout.simple_list_item_1,data);
            lv3.setAdapter(adapter);
        }

    }
}
