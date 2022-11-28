package com.example.weatherdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.weatherdemo.Fragment.Music;
import com.example.weatherdemo.Fragment.News;
import com.example.weatherdemo.Fragment.Users;
import com.example.weatherdemo.Fragment.Weather;
import com.example.weatherdemo.bean.CityInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
//import com.tencent.connect.common.Constants;
//import com.tencent.tauth.Tencent;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Weatherdemo extends AppCompatActivity {
    private News news;
    private Music music;
    private Users users;
    private Weather weather;
//    private static String cityName="上海";
    private ImageView loginimage;
    private LinearLayout linearLayout;
    private String tem;
    static String city1;
    private String mmtem;
    private String humidity;
    private String quality;
    private String wendu;
    BottomNavigationView bottomNavigationView = null;
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private static Weatherdemo instance;
    private static final String TAG1 = "QQLogin";
    private static final String APP_ID = "1105658914";
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    String url;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.index);
        mTencent = Tencent.createInstance(APP_ID,Weatherdemo.this.getApplicationContext());
        linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.user,null);


        LocationClient.setAgreePrivacy(true);
        try {
            mLocationClient = new LocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationClient.registerLocationListener(myListener);
//        // 权限请求
        getPermissionMethod();

    }
    public  void getlocation(String city){
        String[] cityCodeArray = getResources().getStringArray(R.array.cityCode);
        String[] cityNameArray = getResources().getStringArray(R.array.cityName);
        List<String> cityNameList = Arrays.asList(cityNameArray);
        int index = cityNameList.indexOf(city);
        if (index == -1) {
            Toast.makeText(Weatherdemo.this, "输入城市名称未录入系统,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
       city1 = city;
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
                                initFragment();

                            }
                            @Override
                            public void onError(ApiException e) {
                                // 处理异常
                            }
                        }
                ) {
                });

    }
    public void buttonLogin(View v){
        linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.user,null);
        loginimage = linearLayout.findViewById(R.id.loginimage);
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(Weatherdemo.this,"all", mIUiListener);
        Glide.with(Weatherdemo.this).load(url).into(loginimage);

    }
    public static Weatherdemo getInstance(){
        // 因为我们程序运行后，Application是首先初始化的，如果在这里不用判断instance是否为空
        return instance;
    }

    void initFragment() {
        //初始化3个页面
        weather = new Weather(tem,city1,mmtem,humidity,quality,wendu);
        news = new News();
        music = new Music();
        users = new Users();

        transaction.add(R.id.Main_fragment, weather);
        transaction.add(R.id.Main_fragment, news);
        transaction.add(R.id.Main_fragment, music);
        transaction.add(R.id.Main_fragment,  users);

        hideAllFragment(transaction);//隐藏全部界面

        //默认显示第一个选项卡
        transaction.show(weather);
        transaction.commit();//show()后一定要commit()

        //布局中的底部bottom_navigation
        bottomNavigationView = findViewById(R.id.BottomNavigation);
        //设置底部三个选项卡按钮进行切换的事件
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);//先隐藏全部界面 再根据按钮显示
            switch (item.getItemId()) {
                case R.id.menu_weather:
                    transaction.show(weather);
                    transaction.commit();
                    return true;
                case R.id.menu_news:
                    transaction.show(news);
                    transaction.commit();
                    return true;
                case R.id.menu_music:
                    transaction.show(music);
                    transaction.commit();
                    return true;
                case R.id.menu_user:
                    transaction.show(users);
                    transaction.commit();
                    return true;
            }
            return false;
        });
    }
    void hideAllFragment(FragmentTransaction transaction) {
        if (weather != null)
            transaction.hide(weather);
        if (news != null)
            transaction.hide(news);
        if (music != null)
            transaction.hide(music);
        if (users != null)
            transaction.hide(users);
    }
    private void dealWithData(ResponseData data) {

        if (data == null) {
            Toast.makeText(Weatherdemo.this, "结果异常！", Toast.LENGTH_SHORT).show();
            return;
        }
        Forecast todayForecast = data.getForecast().get(0);
//        shidu = data.getShidu();
        quality = "空气质量   "+data.getQuality();
        wendu = data.getWendu()+"°";
        city1 = "上海";
        tem = todayForecast.getType();
        mmtem = "最"+todayForecast.getLow()+"\n"+"最"+todayForecast.getHigh();
        humidity = "风况          "+todayForecast.getFl();


    }

//    private String buildForecastString(Forecast forecast, String day) {
//        return day + "【" + cityName + "】天气" + "\n" +
//                "日期：" + forecast.getYmd() + "丨" +
//                forecast.getWeek() + ";\n" +
//                "天气类型：" + forecast.getType() + ";\n" +
//                "最高温度：" + forecast.getHigh() + ";\n" +
//                "最低温度：" + forecast.getLow() + ";\n" +
//                "刮风情况：" + forecast.getFl() + "丨" +
//                forecast.getFx() +
//                ";\n" +
//                "天气建议：" + forecast.getNotice() + ";\n\n\n";
//    }
    private void getPermissionMethod() {
        List<String> permissionList = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(Weatherdemo.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        Log.i(TAG, "getPermissionMethod: permissionListSize:"+permissionList.size());
        if (!permissionList.isEmpty()){ //权限列表不是空
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Weatherdemo.this,permissions,1);
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

    public  class MyLocationListener extends BDAbstractLocationListener {
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
            if(city!=null) {
                city = city.replace("市","");
                getlocation(city);

            }
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

private class BaseUiListener implements IUiListener {

    @Override
    public void onComplete(Object response) {
        Toast.makeText(Weatherdemo.this, "授权成功", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "response:" + response);
        JSONObject obj = (JSONObject) response;
        linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.user,null);
        loginimage = linearLayout.findViewById(R.id.loginimage);
        Button button = linearLayout.findViewById(R.id.loginbutton);
        try {
            String openID = obj.getString("openid");
            String accessToken = obj.getString("access_token");
            String expires = obj.getString("expires_in");
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(accessToken,expires);
            QQToken qqToken = mTencent.getQQToken();
            mUserInfo = new UserInfo(getApplicationContext(),qqToken);
            mUserInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    Log.e(TAG1,"登录成功"+response.toString());
                    JSONObject obj = (JSONObject) response;

                    try {
                        String url = obj.getString("figureurl_qq_1");
                        Glide.with(Weatherdemo.this).load(url).into(loginimage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    Log.e(TAG1,"登录失败"+uiError.toString());
                }

                @Override
                public void onCancel() {
                    Log.e(TAG1,"登录取消");

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(Weatherdemo.this, "授权失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancel() {
        Toast.makeText(Weatherdemo.this, "授权取消", Toast.LENGTH_SHORT).show();

    }

}

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}