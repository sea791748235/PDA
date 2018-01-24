package com.thb.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.thb.app.APPMainActivity;
import com.thb.app.R;
import com.thb.ws.BaanSoapService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class LOGINMainActivity extends FragmentActivity {

    private String usernameCheck;
    private String passwordCheck;
    private String companyCheck;

    private int itemNo=0;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private BaanSoapService service;
    //private WSSoapService service;
    private Handler mHandler;
    private boolean isConn=false;
    private URL url;
    private HttpURLConnection conn=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        //initHandler();
        //choose company


        //is web connect
        //isWebConn();

        //if(isConn){
       //     Toast.makeText(getApplicationContext(),"未连接服务器",Toast.LENGTH_SHORT).show();
      //  }else {
      //      Toast.makeText(getApplicationContext(),"已连接服务器",Toast.LENGTH_SHORT).show();
      //  }
        //login
    }
}