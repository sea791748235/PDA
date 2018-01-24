package com.thb.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.text.AllCapsTransformationMethod;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thb.app.APPMainActivity;
import com.thb.app.R;
import com.thb.ws.BaanJsonOperator;
import com.thb.ws.ComWSUtil;
import com.thb.ws.WSUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by sea79 on 2018/1/4.
 */

public class ComEditText extends AppCompatEditText{

    private final static String DEBUG_TAG = "ComEditText";
    private Drawable imgInable;
    private Drawable imgAble;
    private Context mContext;
    //private Context mainContext;
    private APPMainActivity mainContext;
    private UIUtil uiUtil=new UIUtil();
    /**
     * 传入的参数
     */
    private String method;
    private Map activaMap;
    private Map paramsMap;
    public CETWSCallBack cetwsCallBack;
    public CETQRCallBack cetqrCallBack;
    boolean isWS=true;
    boolean isQR=true;

    public ComEditText(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ComEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public ComEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setIn(String method, Map activaMap, Map paramsMap) {
        this.method = method;
        this.activaMap = activaMap;
        this.paramsMap = paramsMap;
    }

    public void setMainContext(APPMainActivity mainContext){
        this.mainContext= mainContext;
    }

    public void setCetwsCallBack(CETWSCallBack cetwsCallBack){
        this.cetwsCallBack=cetwsCallBack;
    }

    public void setCetqrCallBack(CETQRCallBack cetqrCallBack){
        this.cetqrCallBack=cetqrCallBack;
    }

    private void init() {

        setEditAble(true);
        setSingleLine(true);
        setSelectAllOnFocus(true);
        setTransformationMethod(new ComCapTransMethod());
        if(uiUtil.getSystemModel().contains("C4050")){
            uiUtil.hideKeyboard(this);
        }
        imgInable = mContext.getResources().getDrawable(R.mipmap.delete_gray);
        imgAble = mContext.getResources().getDrawable(R.mipmap.delete);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //setDrawable();
            }
        });
        //setDrawable();
    }

    public void setEditAble(boolean isEnable){
        if(isEnable){
            setFocusableInTouchMode(true);
            setFocusable(true);
            setBackgroundResource(R.drawable.et_shapes);
        }else {
            setFocusableInTouchMode(false);
            setFocusable(false);
            setBackgroundResource(0);
        }
    }

    public void setIsWS(boolean isWS){
        this.isWS=isWS;
    }

    public void setIsQR(boolean isQR){
        this.isQR=isQR;
    }

    //设置删除图片
    private void setDrawable() {
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
    }

    // 处理删除事件
    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            //Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }
    */

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //super.onKeyDown(keyCode,event);

        if(uiUtil.getSystemModel().contains("C4050")){
            if(keyCode==139) {
                if (isQR) {
                    doQR();
                    return false;
                }
                return true;
            }

            if(keyCode == 66) {
                if(isWS){
                        doWS();
                        Log.e("CET&&&&&&&&&&&&","&&&&&&&&&&&&&&&&");
                        return false;
                    }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doQR(){
        mainContext.doScan(new Barcode2DWithSoft.ScanCallback() {
            @Override
            public void onScanComplete(int i, int i1, byte[] bytes) {
                mainContext.mReader.stopScan();
                if (i1 > 0) {
                    String qrResult = new String(bytes).trim();
                    setText(qrResult);
                    selectAll();
                } else {
                    Toast.makeText(getContext(), "0:扫描失败", Toast.LENGTH_SHORT).show();
                    uiUtil.errorSound(getContext());
                }
            }

        });
    }

    public void doWS(){
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.show();

        WSUtil.CallWS(null, null, method, activaMap, paramsMap, new WSUtil.WSCallBack() {
            @Override
            public void callBack(String result) {
                JSONObject jsonObject = null;
                String info = null;
                if (Character.isDigit(result.charAt(0))) {
                    if(!Character.isLetterOrDigit(result.charAt(3))&&!uiUtil.isCharCN(result.charAt(3))){
                        info="0:输入格式错误";
                        Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
                    }else {
                        info = result;
                        Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        JSONObject obj = JSON.parseObject(result);
                        JSONArray noArr = (JSONArray) JSON.parse(obj.get("Result").toString());
                        if (noArr.isEmpty()) {
                            info = "0:返回信息为空";
                            Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
                        } else {
                            String strTemp = "";
                            for (Object no : noArr) {
                                strTemp = no.toString();
                                jsonObject = JSON.parseObject(strTemp);
                                Log.e(DEBUG_TAG, strTemp);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, e.getMessage());
                        info = "0：JSON转换错误";
                        Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
                    } finally {
                        cetwsCallBack.wscallBack(info,jsonObject);
                    }
                }
                if (info != null && info.substring(0, 1).equals("0")) {
                    uiUtil.errorSound(mContext);
                }else {
                    uiUtil.successSound(mContext);
                }
                cetwsCallBack.wscallBack(info,jsonObject);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },100);
            }
        });
    }

    /**
     * 回调接口
     */
    public interface CETWSCallBack {
        void wscallBack(String info,JSONObject jsonObject);
    }

    public interface CETQRCallBack{
        void qrcallBack(String qrResult);
    }
}
