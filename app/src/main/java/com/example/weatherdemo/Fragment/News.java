package com.example.weatherdemo.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.example.weatherdemo.bean.Newsbean;

import com.example.weatherdemo.bean.StudentNews;
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
    String url, title, content;
//    List<Newsbean> newsBeanArrayList = new ArrayList<>();
    static List<StudentNews> newsBeanArrayList = new ArrayList<>();
    private static ListView lv;
    ListView lv2;
    private static ArrayAdapter adapter;
    private WebView webView;

//    private NewsHandler newsHandler;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, container, false);
//上面的第一个参数为"另一xml"名称
        lv = view.findViewById(R.id.news_ListView_a);
        webView = view.findViewById(R.id.webview);
        lv2 = view.findViewById(R.id.news_ListView_b);
        GetNuatureNewsTask  getNuatureNewsTask = new GetNuatureNewsTask ();
        getNuatureNewsTask .execute();


            jiexiJson jiexiJ = new jiexiJson();
            String str = jiexiJ.getJson();
            Gson gson = new Gson();
        List<Newsbean>   newsBeanArray = gson.fromJson(str,new TypeToken<List<Newsbean>>(){}.getType());
        lv2 = view.findViewById(R.id.news_ListView_b);
            String[] data = new String[1000];
            int i =0;
            for(Newsbean news: newsBeanArray){
                   data[i++] = news.getTitle();
           }
        ArrayAdapter adapter1=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,data);
        lv2.setAdapter(adapter1);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//          Newsbean item = new Newsbean();
            StudentNews item = new StudentNews();
            Intent intent;

          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                         item = newsBeanArrayList.get(position);
//                         String url = item.getOriginal_text_url();
                          String url = item.getUrl();
                         webView.loadUrl(url);
//                        WebSettings webSettings = webView.getSettings();
//                        webSettings.setJavaScriptEnabled(true);
//                         intent.setAction("android.intent.action.VIEW");
//                         Uri content_url = Uri.parse(url);
//                         intent.setData(content_url);
//                         startActivity(intent);
          }
      });
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            Newsbean item = new Newsbean();


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                item = newsBeanArray.get(position);
                String url = item.getOriginal_text_url();
                webView.loadUrl(url);
//                        WebSettings webSettings = webView.getSettings();
//                        webSettings.setJavaScriptEnabled(true);
//                         intent.setAction("android.intent.action.VIEW");
//                         Uri content_url = Uri.parse(url);
//                         intent.setData(content_url);
//                         startActivity(intent);
            }
        });



        return view;
    }


//    public static String getJson(String fileName, Context context) {
//        //将json数据变成字符串
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            //获取assets资源管理器
//            AssetManager assetManager = context.getAssets();
//            //通过管理器打开文件并读取
//            BufferedReader bf = new BufferedReader(new InputStreamReader(
//                    assetManager.open(fileName)));
//            String line;
//            while ((line = bf.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringBuilder.toString();
//
//    }
   public static class GetNuatureNewsTask extends AsyncTask<Void, Void, List<StudentNews>>  {
    @Override
    protected List<StudentNews> doInBackground(Void... voids) {
        Httpunit httpunit = new Httpunit();
        try {
             newsBeanArrayList = httpunit.get_NatureNews();
                String[] data = new String[1000];
                int i =0;
                for(StudentNews news: newsBeanArrayList){
                    data[i++] = news.getTitle();
                }
                adapter=new ArrayAdapter(Weatherdemo.getInstance(),android.R.layout.simple_list_item_1,data);
                lv.setAdapter(adapter);
            return newsBeanArrayList;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    protected void onPostExecute(List<StudentNews> s) {
        super.onPostExecute(s);
//        Message message = new Message();
//        message.what = 0;
//        newsHandler.sendMessage(message);
    }

  }
//    public class NewsHandler extends Handler {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 0){
//                String[] data = new String[1000];
//                int i =0;
//                for(StudentNews news: newsBeanArrayList){
//                    data[i++] = news.getTitle();
//                }
//                adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,data);
//                lv.setAdapter(adapter);
//            }
//        }
//    }
}
