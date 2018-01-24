package com.thb.ws;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sea79 on 2018/1/4.
 */

public class Handler1 implements Runnable {

    private Handler handler;
    private RefreshUI refreshUI;
    private byte[] data=null;

    public interface RefreshUI{
        public void setImg(byte[] data);
    }

    public void setRefreshUI(RefreshUI refreshUI){
        this.refreshUI=refreshUI;
    }

    public Handler1(Handler handler){
        this.handler=handler;
    }

    @Override
    public void run(){
        Bitmap bitmap=null;
        try{
            URL url=new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515046043365&di=d7fe36abdb073dd9405ad00c2eb6c4a6&imgtype=0&src=http%3A%2F%2Fwww.th7.cn%2Fd%2Ffile%2Fp%2F2016%2F03%2F14%2F8b04f10830d6766c2128125a14e7c47d.jpg");
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000*5);
            if(conn.getResponseCode()==200){
                InputStream is=conn.getInputStream();
                bitmap= BitmapFactory.decodeStream(is);

            }
        }catch (Exception e){
            Log.e("Handler1***",e.getMessage());
        }


    }
}
