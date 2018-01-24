package com.thb.ws;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sea79 on 2018/1/4.
 */

public class Handler2 extends Thread {

    private String url;
    private Handler mHandler;

    public Handler2(String url,Handler mHandler){
        this.url=url;
        this.mHandler=mHandler;
    }

    @Override
    public void run(){
        try{
            HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(1000*5);
            if(conn.getResponseCode()==200){
                InputStream is=conn.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                mHandler.obtainMessage(1,bitmap).sendToTarget();
            }
        }catch (Exception e){
            Log.e("handler2***",e.getMessage());
        }
    }
}
