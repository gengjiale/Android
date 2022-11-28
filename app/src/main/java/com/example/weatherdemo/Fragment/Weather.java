package com.example.weatherdemo.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weatherdemo.MainActivity;
import com.example.weatherdemo.R;
import com.example.weatherdemo.Weatherdemo;
import com.example.weatherdemo.bean.Body;
import com.example.weatherdemo.bean.Forecast;
import com.example.weatherdemo.bean.ResponseData;
import com.example.weatherdemo.bean.StudentNews;
import com.example.weatherdemo.bean.USSTNews;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import httpunit.Httpunit;

public class Weather extends Fragment {
    TextView  temView,cityView,mmtemView,humidityView,weaView,levelView;
    String tem,city,mmtem,humidity,quality,wendu;;
    ImageView weaImageView;
    static List<USSTNews> usstNews = new ArrayList<>();
    private static ListView lv4;
    private static ArrayAdapter adapter;
    Intent i;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_weather, container, false);
        lv4 = view.findViewById(R.id.USSTnewsListView);
        GetUSSTNews usst = new GetUSSTNews("getUSSTNews");
        usst.execute();
        temView = view.findViewById(R.id.temView);
        temView.setText(wendu);
        cityView = view.findViewById(R.id.cityView);
        cityView.setText(city);
        mmtemView = view.findViewById(R.id.mmtemView);
        mmtemView.setText(mmtem);
        weaView = view.findViewById(R.id.weaView);
        weaView.setText(tem);
        humidityView = view.findViewById(R.id.humidityView);
        humidityView.setText(humidity);
        levelView = view.findViewById(R.id.levelView);
        levelView.setText(quality);
        weaImageView = view.findViewById(R.id.weaImageView);
        switch (tem){
            case "阴":
            case "多云":
                weaImageView.setImageResource(R.drawable.b_yin);
                break;
            case "中雨":
            case "大雨":
                weaImageView.setImageResource(R.drawable.b_dayu);
                break;
            case "晴":
                weaImageView.setImageResource(R.drawable.b_qing);
                break;
            case "小雨":
                weaImageView.setImageResource(R.drawable.b_xioayu);
                break;
        }

        lv4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            USSTNews item = new USSTNews();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = usstNews.get(position);
                String url = item.getUrl();
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



        //上面的第一个参数为"另一xml"名称
        return view;
    }
    public Weather(String tem, String city, String mmtem, String humidity,String quality,String wendu) {
        this.tem = tem;
        this.city = city;
        this.mmtem = mmtem;
        this.humidity = humidity;
        this.quality = quality;
        this.wendu = wendu;
    }
    public Weather(){

    }
    public static class GetUSSTNews extends AsyncTask<Void, Void, List<USSTNews>> {
        String newsname="";
        GetUSSTNews(String str) {
            newsname= str;
        }
        GetUSSTNews(){

        }
        @Override
        protected List<USSTNews> doInBackground(Void... voids) {
            Httpunit httpunit = new Httpunit();
            try {
                usstNews = httpunit.get_USSTNews(newsname);
                return usstNews;
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }
        }
        @Override
        protected void onPostExecute(List<USSTNews> s) {
            super.onPostExecute(s);
            String[] data = new String[1000];
            int i =0;
            for(USSTNews news: usstNews){
                data[i++] = news.getTitle();
            }
            adapter=new ArrayAdapter(Weatherdemo.getInstance(),android.R.layout.simple_list_item_1,data);
            lv4.setAdapter(adapter);
        }
    }
}
