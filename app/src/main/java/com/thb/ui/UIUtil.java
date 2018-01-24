package com.thb.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;

import com.thb.app.R;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sea79 on 2017/12/25.
 */

public class UIUtil {

    //获取系统时间
    public String getSysTime(){
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyMMdd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMdd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        final String dateStr1 = dateFormat1.format(date);
        final String dateStr2 = dateFormat2.format(date);
        return dateStr1;
    }

    //隐藏软键盘
    public void hideKeyboard(EditText et) {
        if (Build.VERSION.SDK_INT <= 10) {
            et.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception e) {

            }

        }
    }

    public void startAni(View view,Intent intent){
        AlphaAnimation aa=new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //进度条动画
    public void progressAnimator(final View v){

        PropertyValuesHolder animator=PropertyValuesHolder.ofFloat("scaleX",0.5f,1f);
        PropertyValuesHolder animator2=PropertyValuesHolder.ofFloat("scaleY",0.5f,1f);
        ObjectAnimator animator3=ObjectAnimator.ofPropertyValuesHolder(v,animator,animator2);
        animator3.setDuration(700);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    //失败提示音
    public void errorSound(Context context){
        /*
        //Ringstone播放系统提示音
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone rt=RingtoneManager.getRingtone(context,uri);
        rt.play();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rt.stop();
            }
        },400);
        */

        final MediaPlayer mp= MediaPlayer.create(context, R.raw.beep);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.stop();
            }
        },500);
    }

    //成功提示音
    public void successSound(Context context) {

        /*
        //Ringstone播放系统提示音
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone rt=RingtoneManager.getRingtone(context,uri);
        rt.play();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rt.stop();
            }
        },400);
        */

        final MediaPlayer mp = MediaPlayer.create(context, R.raw.elara);
        mp.start();
    }


    public boolean isPositiveNumber(String str){
        return isPositiveDouble(str)||isPositiveInteger(str);
    }

    public boolean isPositiveInteger(String str){
        try{
            if(Integer.parseInt(str)!=0){
                Log.e("UIPI*********",Integer.parseInt(str)+"");
                return true;
            }else {
                return false;
            }
        }catch (NumberFormatException e){
            return false;
        }
    }

    public boolean isPositiveDouble(String str){
        try{
            if(Double.parseDouble(str)!=0){
                Log.e("UID*********",Double.parseDouble(str)+"");
                return true;
            }else {
                return false;
            }
        }catch (NumberFormatException e){
            return false;
        }
    }


    public boolean isMessCode(String str){
        //String 判全数字
        Pattern p1 = Pattern.compile("[0-9]*");
        Matcher m1 = p1.matcher(str);

        //String 判全字母
        Pattern p2=Pattern.compile("[a-zA-Z]");
        Matcher m2=p2.matcher(str);

        //String 判全中文
        Pattern p3=Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m3=p3.matcher(str);
        if(!m1.matches()||!m2.matches()||!m3.matches()){
            return true;
        }else {
            return false;
        }
    }

    public boolean isCharCN(char c) {
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
    }

    public boolean isChinese(String str){
        if(str==null){
            return false;
        }else {
            for (char c:str.toCharArray()){
                if(c >= 0x4E00 && c <= 0x9FA5){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    public String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

}
