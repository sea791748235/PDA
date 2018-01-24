package com.thb.utils;

/**
 * Created by sea79 on 2017/10/30.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Uncaught异常处理，发送错误报告
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
    public static final String TAG="CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE=new CrashHandler();

    private Context mContext;
    private Map<String,String> infos=new HashMap<String, String>();

    //格式化日期，纪录日志文件时间戳
    private DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    //保证只有一个CrashHandler实例
    private CrashHandler(){

    }

    //单例模式获取CrashHandler实例
    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init(Context context){
        mContext=context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler=Thread.getDefaultUncaughtExceptionHandler();
        //设置CrashHandler为程序默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 异常发生时进行处理
     */
    @Override
    public void uncaughtException(Thread thread,Throwable ex){
        if(!handleException(ex)&&mDefaultHandler!=null){
            //若用户未处理则以系统默认异常处理器执行
            mDefaultHandler.uncaughtException(thread,ex);
        }else {
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                Log.d(TAG,e.getMessage());
            }

            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理，发送错误报告
     */
    private boolean handleException(Throwable ex){
        if(ex==null){
            return false;
        }

        final String strTemp=ex.getMessage();
        //Toast 显示异常信息
        new Thread(){
            @Override
            public void run(){
                Looper.prepare();
                Toast toast=Toast.makeText(mContext,"系统异常，即将退出，错误信息："+strTemp,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Looper.loop();

            }
        }.start();
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Context context){
        try{
            PackageManager pm=context.getPackageManager();
            PackageInfo pi=pm.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
            if(pi!=null){
                String versionName=pi.versionName==null?"null":pi.versionName;
                String versionCode=pi.versionCode+"";
                infos.put("versionName",versionName);
                infos.put("versionCode",versionCode);
            }
        }catch (PackageManager.NameNotFoundException e){
            Log.d(TAG,e.getMessage());
        }

        Field[] fields= Build.class.getDeclaredFields();
        for(Field field:fields){
            try{
                field.setAccessible(true);
                infos.put(field.getName(),field.get(null).toString());
                Log.d(TAG,field.getName()+":"+field.get(null));
            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     */
    private String saveCrashInfo2File(Throwable ex){
        StringBuffer sb=new StringBuffer();
        for(Map.Entry<String,String> entry:infos.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            sb.append(key+"="+value+"\n");
        }

        Writer writer=new StringWriter();
        PrintWriter printWriter=new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause=ex.getCause();
        while (cause!=null){
            cause.printStackTrace(printWriter);
            cause=cause.getCause();
        }
        printWriter.close();
        String result=writer.toString();
        sb.append(result);
        try{
            long timestamp=System.currentTimeMillis();
            String time =dateFormat.format(new Date());
            String fileName="crash--"+time+"--"+timestamp+".log";

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/crash";
                File dir=new File(path);
                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file =new File(path,fileName);
                fileName=path+File.separator+fileName;

                FileOutputStream fos=new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }
            return fileName;
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return null;


    }
}
