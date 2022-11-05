package com.example.terminal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView news_inSchool_text;
    private TextView news_outSchool_text;
    private ImageView news_inSchool_Icon;
    private ImageView news_outSchool_Icon;
    private ImageView back;
    private LinearLayout index;
    private LinearLayout index_newsshow;
    private LinearLayout user;
    private LinearLayout tabbar_index;
    private LinearLayout tabbar_user;

    private final int REQUEST_CODE = -1;

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

        checkPermission(this);

        initial();

    }


    public void initial(){
        Vector<String> text_outSchool = new Vector<String>();
        showText(new File("C:\\Users\\g'j'l\\Desktop\\student_News"), text_outSchool);
        File tempFile =new File(text_outSchool.get(0).trim());
//        File tempFile =new File(showText2("src/main/res/student_News")[0].trim());
        String fileName = tempFile.getName();
        news_outSchool_text.setText(fileName);
    }
    public void showText(File file, Vector<String> vector){
        File[] files = file.listFiles();
            for(File a:files){
                String filePath = a.getAbsolutePath().toLowerCase();
                vector.add(filePath);
                if(a.isDirectory()){
                    showText(a, vector);
                }
            }

    }
//    public String[] showText2(String path){
//        return new File(path).list();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        initial();
//    }

//    private void requestmanageexternalstorage_Permission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // 先判断有没有权限
//            if (Environment.isExternalStorageManager()) {
//                Toast.makeText(this, "Android VERSION  R OR ABOVE，HAVE MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Android VERSION  R OR ABOVE，NO MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.setData(Uri.parse("package:" + this.getPackageName()));
//                startActivityForResult(intent, REQUEST_CODE);
//            }
//        }
//    }

    private void checkPermission(Activity activity) {//开启本地的照片读取与写入权限
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,//读内存权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE};//写内存权限

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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