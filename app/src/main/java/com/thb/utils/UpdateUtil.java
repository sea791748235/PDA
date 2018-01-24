package com.thb.utils;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.thb.login.LOGINTestActivity;

import org.ksoap2.serialization.PropertyInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sea79 on 2017/12/4.
 */

public class UpdateUtil {

    private static final String DEBUG_TAG="UpdateUtil********";

    private DownloadManager dm;

    private long updateId;

    private Context context;
    private String url;



    public UpdateUtil(Context context,String url){
        this.context=context;
        this.url=url;
    }

    public void updateAPK(){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url+"/pda.apk"));
        request.setAllowedOverRoaming(false);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("PDA条码系统更新！！！");
        request.setDescription("PDA Update");
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"pda.apk");

        dm=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

        updateId=dm.enqueue(request);

        context.registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    private void checkStatus(){
        DownloadManager.Query query=new DownloadManager.Query();

        query.setFilterById(updateId);
        Cursor c=dm.query(query);
        if(c.moveToFirst()){
            int status=c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status){
                case DownloadManager.STATUS_PAUSED:
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    installAPK();
                    break;
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(context,"0:下载失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        c.close();
    }

    private void installAPK(){
        Uri uri=dm.getUriForDownloadedFile(updateId);
        if(uri!=null){
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri,"application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            context.unregisterReceiver(receiver);
        }
    }

    public void update(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Looper.prepare();
                    final ProgressDialog pd=new ProgressDialog(context);
                    pd.show();
                    checkVer();
                    pd.dismiss();
                    Looper.loop();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    public void checkVer(){

        String path=url+"/update.txt";
        StringBuffer sb=new StringBuffer();
        String line;
        BufferedReader reader=null;

        try {
            URL url=new URL(path);
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
            //urlConnection.setConnectTimeout(1000*5);
            if(urlConnection.getResponseCode()==200){
                reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
            }else{
                Toast.makeText(context,"0:未连接到服务器",Toast.LENGTH_SHORT).show();
                return;
            }


        }catch (Exception e){
            Toast.makeText(context,"0:未连接到服务器",Toast.LENGTH_SHORT).show();
            Log.e(DEBUG_TAG,e.getMessage());
            return;
        }finally {

            try{
                if(reader!=null){
                    reader.close();
                }
            }catch (Exception e){

                Toast.makeText(context,"0:未连接到服务器",Toast.LENGTH_SHORT).show();
                Log.e(DEBUG_TAG,e.getMessage());
                return;
            }
        }

        String info=sb.toString();

        Log.e(DEBUG_TAG,info);

        String nowVer=null;

        try{
            PackageManager pm=context.getPackageManager();
            PackageInfo pi=pm.getPackageInfo(context.getPackageName(),0);
            nowVer=pi.versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.getMessage();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
        if (nowVer.equals(info)){
            Toast.makeText(context,"0:无需更新",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"1:开始更新",Toast.LENGTH_SHORT).show();
            updateAPK();
        }

    }

}
