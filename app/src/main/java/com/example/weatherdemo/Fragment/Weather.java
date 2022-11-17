package com.example.weatherdemo.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.weatherdemo.bean.Body;
import com.example.weatherdemo.bean.Forecast;
import com.example.weatherdemo.bean.ResponseData;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Weather extends Fragment {
    TextView  temView,cityView,mmtemView,humidityView,weaView,levelView;
    String tem,city,mmtem,humidity,quality,wendu;;
    ImageView weaImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_weather, container, false);

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
                weaImageView.setImageResource(R.drawable.b_yin);
                break;
            case "中雨":
                weaImageView.setImageResource(R.drawable.b_dayu);
                break;
            case "晴":
                weaImageView.setImageResource(R.drawable.b_qing);
                break;
        }



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
}
