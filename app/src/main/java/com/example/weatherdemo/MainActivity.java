package com.example.weatherdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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

import cn.edu.heuet.editspinner.EditSpinner;

public class MainActivity extends AppCompatActivity {
    private String cityName;
    private TextView tvContent;
    EditSpinner editSpinner;
    // 数组的长度为2代表只记录双击操作
    private static final long[] TIME_ARRAY = new long[2];

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationClient.setAgreePrivacy(true);
        try {
            mLocationClient = new LocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationClient.registerLocationListener(myListener);
//        // 权限请求
        getPermissionMethod();

        setContentView(R.layout.activity_main);
        // 1、读取 XML 数据
        String[] cityCodeArray = getResources().getStringArray(R.array.cityCode);

        String[] cityNameArray = getResources().getStringArray(R.array.cityName);
        List<String> cityNameList = Arrays.asList(cityNameArray);

        // 2、将数据填充给 Spinner 控件
        editSpinner = findViewById(R.id.editspinner);
        editSpinner.setItemData(cityNameList);
        tvContent = findViewById(R.id.tv_content);
        Button btSearch = findViewById(R.id.bt_search);


        // 3、按钮点击响应事件
        btSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 防止快速点击
                if (isOnDoubleClick(3000)) {
                    Toast.makeText(MainActivity.this, "查询太频繁，手别抖！", Toast.LENGTH_SHORT).show();
                    return;
                }
                cityName = editSpinner.getText();
                if (TextUtils.isEmpty(cityName)) {
                    return;
                }
                // 根据填写的城市名称查到对应的城市代码
                int index = cityNameList.indexOf(cityName);
                if (index == -1) {
                    Toast.makeText(MainActivity.this, "输入城市名称未录入系统,请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "http://t.weather.itboy.net/api/weather/city/" + cityCodeArray[index];
                // Xhttp2 请求网络
                XHttp.get(url)
                        .syncRequest(false)
                        .execute(new CallBackProxy<Body<ResponseData>, ResponseData>(
                                new SimpleCallBack<ResponseData>() {
                                    @Override
                                    public void onSuccess(ResponseData data) {
                                        // 处理 data
                                        dealWithData(data);
                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        // 处理异常
                                    }
                                }
                        ) {
                        });

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void dealWithData(ResponseData data) {
        if (data == null) {
            Toast.makeText(MainActivity.this, "结果异常！", Toast.LENGTH_SHORT).show();
            return;
        }
        Forecast todayForecast = data.getForecast().get(0);
        String today = buildForecastString(todayForecast, "今天");

        Forecast tomorrowForecast = data.getForecast().get(1);
        String tomorrow = buildForecastString(tomorrowForecast, "明天");

        tvContent.setText(today + tomorrow);
    }

    private String buildForecastString(Forecast forecast, String day) {
        return day + "【" + cityName + "】天气" + "\n" +
                "日期：" + forecast.getYmd() + "丨" +
                forecast.getWeek() + ";\n" +
                "天气类型：" + forecast.getType() + ";\n" +
                "最高温度：" + forecast.getHigh() + ";\n" +
                "最低温度：" + forecast.getLow() + ";\n" +
                "刮风情况：" + forecast.getFl() + "丨" +
                forecast.getFx() +
                ";\n" +
                "天气建议：" + forecast.getNotice() + ";\n\n\n";
    }
    //是否在短时间内进行了双击操作
    public  boolean isOnDoubleClick(int time) {
        System.arraycopy(TIME_ARRAY, 1, TIME_ARRAY, 0, TIME_ARRAY.length - 1);
        TIME_ARRAY[TIME_ARRAY.length - 1] = SystemClock.uptimeMillis();
        return TIME_ARRAY[0] >= (SystemClock.uptimeMillis() - time);
    }

    private void getPermissionMethod() {
        List<String> permissionList = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        Log.i(TAG, "getPermissionMethod: permissionListSize:"+permissionList.size());
        if (!permissionList.isEmpty()){ //权限列表不是空
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else{
            Log.i(TAG, "getPermissionMethod: requestLocation !permissionList.isEmpty()里");
            requestLocation();
        }
    }
    //开启 start 定位，默认只启动一次，需要自己设置间隔次数
    private void requestLocation() {
        initLocation();//其他请求设置
        mLocationClient.start();  //定位请求开启
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);  // 定位模式是仅限设备模式，也就是仅允许GPS来定位。
        option.setScanSpan(2000);
        mLocationClient.setLocOption(option);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须统一所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else
                {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private static final String TAG = "MainActivity";

    public static class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation( BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            Log.d(TAG, "run: 纬度："+location.getLatitude());
            String addr = location.getAddrStr();    //获取详细地址信息
            Log.d(TAG, "onReceiveLocation:详细地址信 "+addr);
            String country = location.getCountry();    //获取国家
            Log.d(TAG, "onReceiveLocation: 国家："+country);
            String province = location.getProvince();    //获取省份
            Log.d(TAG, "onReceiveLocation: 省份："+province);
            String city = location.getCity();    //获取城市
//            if(city!=null) {
//                editSpinner.setText(city);
//            }
            Log.d(TAG, "onReceiveLocation: 城市："+city);
            String district = location.getDistrict();    //获取区县

            Log.d(TAG, "onReceiveLocation: 区县信息："+district);
            String street = location.getStreet();    //获取街道信息
            Log.d(TAG, "onReceiveLocation: 街道信息："+street);

            if (location.getLocType() == BDLocation.TypeNetWorkLocation)
                Log.d(TAG, "onReceiveLocation: 定位方式：NET"+location.getLocType());
            else if(location.getLocType() == BDLocation.TypeGpsLocation )
                Log.d(TAG, "onReceiveLocation: 定位方式：GPS"+location.getLocType());
            else
                Log.d(TAG, "onReceiveLocation: 定位出错");
        }

    }

}




