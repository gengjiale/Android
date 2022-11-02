package com.example.terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView news_inSchool_text;
    private TextView news_outSchool_text;
    private ImageView news_inSchool_Icon;
    private ImageView news_outSchool_Icon;
    private ImageView back;
    private LinearLayout index;
    private LinearLayout index_newsshow;
    private LinearLayout user;
    private LinearLayout tabbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //index页面
        index = (LinearLayout) getLayoutInflater().inflate(R.layout.index, null);
        setContentView(index);
        news_inSchool_text = findViewById(R.id.news_inSchool_text);
        news_outSchool_text = findViewById(R.id.news_outSchool_text);
        findViewById(R.id.news_inSchool_Icon).setOnClickListener(this);
        findViewById(R.id.news_outSchool_Icon).setOnClickListener(this);
        //index_newsshow
        index_newsshow = (LinearLayout) getLayoutInflater().inflate(R.layout.index_newsshow, null);
        index_newsshow.findViewById(R.id.back).setOnClickListener(this);
        //user
        user = (LinearLayout) getLayoutInflater().inflate(R.layout.user, null);
        //tabbar
        tabbar = (LinearLayout) getLayoutInflater().inflate(R.layout.tabbar, null);
        tabbar.findViewById(R.id.ll_first).setOnClickListener(this);
        tabbar.findViewById(R.id.ll_second).setOnClickListener(this);
        tabbar.findViewById(R.id.ll_third).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.news_inSchool_Icon){//点击了学校信息
            setContentView(index_newsshow);
        }
        else if(view.getId() == R.id.news_outSchool_Icon){//点击了校外信息

        }
        else if(view.getId() == R.id.back){
            setContentView(index);
        }
        else if(view.getId() == R.id.ll_first){
            setContentView(index);
        }
        else if(view.getId() == R.id.ll_second){

        }
        else if(view.getId() == R.id.ll_third){
            setContentView(user);
        }
    }
}