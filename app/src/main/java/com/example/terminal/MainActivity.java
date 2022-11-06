package com.example.terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView news_inSchool_text;
    private TextView news_outSchool_text;
    private LinearLayout index;
    private LinearLayout index_newsshow;
    private LinearLayout user;
    private JSONObject news_inSchool_json;

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
        findViewById(R.id.ll_first).setOnClickListener(this);
        findViewById(R.id.ll_second).setOnClickListener(this);
        findViewById(R.id.ll_third).setOnClickListener(this);
        //index_newsshow
        index_newsshow = (LinearLayout) getLayoutInflater().inflate(R.layout.index_newsshow, null);
        index_newsshow.findViewById(R.id.back).setOnClickListener(this);
        //user
        user = (LinearLayout) getLayoutInflater().inflate(R.layout.user, null);
        user.findViewById(R.id.ll_first).setOnClickListener(this);
        user.findViewById(R.id.ll_second).setOnClickListener(this);
        user.findViewById(R.id.ll_third).setOnClickListener(this);
        System.out.println("------------------------------111111111111111111111111111111111--------------------------------------");

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            news_inSchool_json =  handle_json("mydata.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(news_inSchool_json.getJSONObject("办事指南"));
        news_inSchool_text.setText((String)news_inSchool_json.getJSONObject("办事指南").get("本科教育教学建设研究与改革系列项目申报指南"));
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private JSONObject handle_json(String file_name) throws IOException {
        AssetManager assetManager = this.getApplicationContext().getAssets();

        InputStream inputStream;
        InputStreamReader isr = null;
        BufferedReader br = null;

        StringBuffer sb =  new StringBuffer();

        inputStream = assetManager.open(file_name);
        isr = new InputStreamReader(inputStream);
        br = new BufferedReader(isr);

        String line;
        while ((line = br.readLine()) != null) {
            sb.append("\n" + line);
        }
        br.close();
        isr.close();
        inputStream.close();

        String jsonStr = sb.toString();
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject;
    }
}