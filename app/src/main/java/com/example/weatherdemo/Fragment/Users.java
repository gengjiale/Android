package com.example.weatherdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherdemo.R;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;
//import com.tencent.connect.UserInfo;
//import com.tencent.connect.auth.QQToken;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class Users extends Fragment {
    ImageView imageView;
    Button button;
    private View view;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user,container,false);
        imageView = view.findViewById(R.id.loginimage);
        button = view.findViewById(R.id.loginbutton);
        button.setText("QQ登录");
        return view;
    }


}
