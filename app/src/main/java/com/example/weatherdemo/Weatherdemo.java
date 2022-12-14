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
//    private static String cityName="??????";
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
//        // ????????????
        getPermissionMethod();

    }
    public  void getlocation(String city){
        String[] cityCodeArray = getResources().getStringArray(R.array.cityCode);
        String[] cityNameArray = getResources().getStringArray(R.array.cityName);
        List<String> cityNameList = Arrays.asList(cityNameArray);
        int index = cityNameList.indexOf(city);
        if (index == -1) {
            Toast.makeText(Weatherdemo.this, "?????????????????????????????????,???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
       city1 = city;
        String url = "http://t.weather.itboy.net/api/weather/city/" + cityCodeArray[index];
        // Xhttp2 ????????????
        XHttp.get(url)
                .syncRequest(false)
                .execute(new CallBackProxy<Body<ResponseData>, ResponseData>(
                        new SimpleCallBack<ResponseData>() {
                            @Override
                            public void onSuccess(ResponseData data) {
                                // ?????? data
                                dealWithData(data);
                                initFragment();

                            }
                            @Override
                            public void onError(ApiException e) {
                                // ????????????
                            }
                        }
                ) {
                });

    }
    public void buttonLogin(View v){
        linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.user,null);
        loginimage = linearLayout.findViewById(R.id.loginimage);
        mIUiListener = new BaseUiListener();
        //all????????????????????????
        mTencent.login(Weatherdemo.this,"all", mIUiListener);
        Glide.with(Weatherdemo.this).load(url).into(loginimage);

    }
    public static Weatherdemo getInstance(){
        // ??????????????????????????????Application???????????????????????????????????????????????????instance????????????
        return instance;
    }

    void initFragment() {
        //?????????3?????????
        weather = new Weather(tem,city1,mmtem,humidity,quality,wendu);
        news = new News();
        music = new Music();
        users = new Users();

        transaction.add(R.id.Main_fragment, weather);
        transaction.add(R.id.Main_fragment, news);
        transaction.add(R.id.Main_fragment, music);
        transaction.add(R.id.Main_fragment,  users);

        hideAllFragment(transaction);//??????????????????

        //??????????????????????????????
        transaction.show(weather);
        transaction.commit();//show()????????????commit()

        //??????????????????bottom_navigation
        bottomNavigationView = findViewById(R.id.BottomNavigation);
        //??????????????????????????????????????????????????????
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);//????????????????????? ?????????????????????
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
            Toast.makeText(Weatherdemo.this, "???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        Forecast todayForecast = data.getForecast().get(0);
//        shidu = data.getShidu();
        quality = "????????????   "+data.getQuality();
        wendu = data.getWendu()+"??";
        city1 = "??????";
        tem = todayForecast.getType();
        mmtem = "???"+todayForecast.getLow()+"\n"+"???"+todayForecast.getHigh();
        humidity = "??????          "+todayForecast.getFl();


    }

//    private String buildForecastString(Forecast forecast, String day) {
//        return day + "???" + cityName + "?????????" + "\n" +
//                "?????????" + forecast.getYmd() + "???" +
//                forecast.getWeek() + ";\n" +
//                "???????????????" + forecast.getType() + ";\n" +
//                "???????????????" + forecast.getHigh() + ";\n" +
//                "???????????????" + forecast.getLow() + ";\n" +
//                "???????????????" + forecast.getFl() + "???" +
//                forecast.getFx() +
//                ";\n" +
//                "???????????????" + forecast.getNotice() + ";\n\n\n";
//    }
    private void getPermissionMethod() {
        List<String> permissionList = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(Weatherdemo.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        Log.i(TAG, "getPermissionMethod: permissionListSize:"+permissionList.size());
        if (!permissionList.isEmpty()){ //?????????????????????
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Weatherdemo.this,permissions,1);
        }else{
            Log.i(TAG, "getPermissionMethod: requestLocation !permissionList.isEmpty()???");
            requestLocation();
        }
    }
    //?????? start ???????????????????????????????????????????????????????????????
    private void requestLocation() {
        initLocation();//??????????????????
        mLocationClient.start();  //??????????????????
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);  // ??????????????????????????????????????????????????????GPS????????????
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
                            Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else
                {
                    Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private static final String TAG = "MainActivity";

    public  class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation( BDLocation location){
            //?????????BDLocation?????????????????????????????????????????????get??????????????????????????????????????????
            //??????????????????????????????????????????????????????
            //??????????????????????????????????????????????????????BDLocation???????????????
            Log.d(TAG, "run: ?????????"+location.getLatitude());
            String addr = location.getAddrStr();    //????????????????????????
            Log.d(TAG, "onReceiveLocation:??????????????? "+addr);
            String country = location.getCountry();    //????????????
            Log.d(TAG, "onReceiveLocation: ?????????"+country);
            String province = location.getProvince();    //????????????
            Log.d(TAG, "onReceiveLocation: ?????????"+province);
            String city = location.getCity();    //????????????
            if(city!=null) {
                city = city.replace("???","");
                getlocation(city);

            }
            Log.d(TAG, "onReceiveLocation: ?????????"+city);
            String district = location.getDistrict();    //????????????

            Log.d(TAG, "onReceiveLocation: ???????????????"+district);
            String street = location.getStreet();    //??????????????????
            Log.d(TAG, "onReceiveLocation: ???????????????"+street);

            if (location.getLocType() == BDLocation.TypeNetWorkLocation)
                Log.d(TAG, "onReceiveLocation: ???????????????NET"+location.getLocType());
            else if(location.getLocType() == BDLocation.TypeGpsLocation )
                Log.d(TAG, "onReceiveLocation: ???????????????GPS"+location.getLocType());
            else
                Log.d(TAG, "onReceiveLocation: ????????????");
        }

    }

private class BaseUiListener implements IUiListener {

    @Override
    public void onComplete(Object response) {
        Toast.makeText(Weatherdemo.this, "????????????", Toast.LENGTH_SHORT).show();
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
                    Log.e(TAG1,"????????????"+response.toString());
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
                    Log.e(TAG1,"????????????"+uiError.toString());
                }

                @Override
                public void onCancel() {
                    Log.e(TAG1,"????????????");

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(Weatherdemo.this, "????????????", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancel() {
        Toast.makeText(Weatherdemo.this, "????????????", Toast.LENGTH_SHORT).show();

    }

}

    /**
     * ?????????Login???Activity??????Fragment?????????onActivityResult??????
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