package com.example.weatherdemo.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherdemo.Jsontool.jiexiJson;
import com.example.weatherdemo.R;
import com.example.weatherdemo.Weatherdemo;
import com.example.weatherdemo.bean.MusicNews;
import com.example.weatherdemo.bean.NatureNews;
import com.example.weatherdemo.bean.Newsbean;

import com.example.weatherdemo.bean.StudentNews;
import com.example.weatherdemo.bean.USSTNews;
import com.google.gson.Gson;
import com.xuexiang.xhttp2.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import httpunit.Httpunit;

public class News extends Fragment {
    private View view;
//    List<Newsbean> newsBeanArrayList = new ArrayList<>();
    static List<StudentNews> studentNews = new ArrayList<>();
    static List<NatureNews> natureNews = new ArrayList<>();
//    static List<USSTNews> usstNews = new ArrayList<>();
//    static List<MusicNews> musicNews = new ArrayList<>();
    private static ListView lv1,lv2,lv3,lv4;
    private static ArrayAdapter adapter;
    private WebView webView;
    Intent i;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news, container, false);
//上面的第一个参数为"另一xml"名称
        lv1 = view.findViewById(R.id.news_ListView_a);
//        webView = view.findViewById(R.id.webview);
        lv2 = view.findViewById(R.id.news_ListView_b);
//        lv4 = view.findViewById(R.id.USSTnewsListView);
//        lv3 = view.findViewById(R.id.Music);
        GetStudentNewsTask  getNewsTask = new GetStudentNewsTask ("getStudentNews");
        getNewsTask .execute();
        GetNatureNews  nature = new GetNatureNews ("getNatureNews");
        nature .execute();

//        jiexiJson jiexiJ = new jiexiJson();
//        String str = jiexiJ.getJson();
//        Gson gson = new Gson();
//        List<Newsbean>   newsBeanArray = gson.fromJson(str,new TypeToken<List<Newsbean>>(){}.getType());
//        lv2 = view.findViewById(R.id.news_ListView_b);
//            String[] data = new String[1000];
//            int i =0;
//            for(Newsbean news: newsBeanArray){
//                   data[i++] = news.getTitle();
//           }
//        ArrayAdapter adapter1=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,data);
//        lv2.setAdapter(adapter1);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            StudentNews item = new StudentNews();
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         item = studentNews.get(position);
                          String url = item.getUrl();
                          i = new Intent(Intent.ACTION_VIEW);
                          i.setData(Uri.parse(url));
                          startActivity(i);
          }
      });
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            NatureNews item = new NatureNews();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = natureNews.get(position);
                String url = item.getOriginal_text_url();
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }


   public static class GetStudentNewsTask extends AsyncTask<Void, Void, List<StudentNews>> {
       String newsname = "";

       GetStudentNewsTask(String str) {
           newsname = str;
       }

       GetStudentNewsTask() {

       }

       @Override
       protected List<StudentNews> doInBackground(Void... voids) {
           Httpunit httpunit = new Httpunit();
           try {
               studentNews = httpunit.get_StudentNews(newsname);
               return studentNews;
           } catch (IOException e) {
               e.printStackTrace();
               return null;
           }
       }

       @Override
       protected void onPostExecute(List<StudentNews> s) {
           super.onPostExecute(s);
           String[] data = new String[1000];
           int i = 0;
           for (StudentNews news : studentNews) {
               data[i++] = news.getTitle();
           }
           adapter = new ArrayAdapter(Weatherdemo.getInstance(), android.R.layout.simple_list_item_1, data);
           lv1.setAdapter(adapter);
       }
   }
   public static class GetNatureNews extends AsyncTask<Void, Void, List<NatureNews>> {
       String newsname = "";

       GetNatureNews(String str) {
           newsname = str;
       }

       GetNatureNews() {

       }

       @Override
       protected List<NatureNews> doInBackground(Void... voids) {
           Httpunit httpunit = new Httpunit();
           try {
               natureNews = httpunit.get_NatureNews(newsname);
               return natureNews;
           } catch (IOException e) {
               e.printStackTrace();
               return null;
           }
       }

       @Override
       protected void onPostExecute(List<NatureNews> s) {
           super.onPostExecute(s);
           String[] data = new String[1000];
           int i = 0;
           for (NatureNews news : natureNews) {
               data[i++] = news.getTitle();
           }
           adapter = new ArrayAdapter(Weatherdemo.getInstance(), android.R.layout.simple_list_item_1, data);
           lv2.setAdapter(adapter);
       }

   }

}
