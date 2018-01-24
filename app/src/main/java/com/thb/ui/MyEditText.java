package com.thb.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thb.app.APPComFragment;
import com.thb.app.APPMainActivity;
import com.thb.app.R;
import com.thb.qr.QRUtil;
import com.thb.ws.BaanJsonOperator;
import com.thb.ws.MOMBean;
import com.thb.ws.TestPub;
import com.thb.ws.WSUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sea79 on 2017/10/30.
 */

public class MyEditText extends AppCompatEditText{

    private final static String DEBUG_TAG="MyEditText";
    private Drawable imgInable;
    private Drawable imgAble;
    private Context mContext;
    private String method;
    private Map activaMap;
    private Map paramsMap;

    private String info=null;
    private JSONObject jsonObject=null;
    /**
     * 传出的Str
     */

    private OnMyEditTextListener listener;
    private OnMyEditorActionListener eaListener;

    public MyEditText(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setInParams(String method,Map activaMap,Map paramsMap){

        this.method=method;
        this.activaMap= activaMap;
        this.paramsMap=paramsMap;

        Log.e(DEBUG_TAG,method+'\n'+activaMap.toString()+'\n'+paramsMap.toString());
    }

    public void setOutParams(String info,JSONObject jsonObject){
        this.info=info;
        this.jsonObject=jsonObject;
    }

    public OnMyEditTextListener getListener() {
        return listener;
    }

    public void setOnMyEditTextListener(OnMyEditTextListener listener) {
        this.listener = listener;
    }

    private void init() {
        imgInable = mContext.getResources().getDrawable(R.mipmap.delete_gray);
        imgAble = mContext.getResources().getDrawable(R.mipmap.delete);
        //imgInable.setBounds(0,0,20,20);
        //imgAble.setBounds(0,0,20,20);
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();


    }

    public void doWS(){
        if (!TextUtils.isEmpty(getText())) {

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.show();
            //new Thread(new Runnable() {
            //    @Override
            //    public void run() {
            //        Looper.prepare();
            //try {
            WSUtil.CallWS(null, null, method, activaMap, paramsMap, new WSUtil.WSCallBack() {
                @Override
                public void callBack(String result) {

                    String infoTemp=null;
                    JSONObject jsonTemp=null;

                    String strTT = result.substring(0,1);
                    if(strTT.equals("0")||strTT.equals("1"))
                    {
                        Toast.makeText(mContext, result  , Toast.LENGTH_SHORT).show();
                        infoTemp=result;

                    }else{
                        try {
                            JSONObject obj = JSON.parseObject(result);
                            JSONArray noArr = (JSONArray) JSON.parse(obj.get("Result").toString());

                            if (noArr.isEmpty()) {
                                infoTemp="0:订单信息错误";
                                Toast.makeText(mContext, infoTemp, Toast.LENGTH_SHORT).show();

                            } else {
                                String strTemp = "";

                                for (Object no : noArr) {
                                    strTemp = no.toString();
                                    jsonTemp = JSON.parseObject(strTemp);
                                    Log.e(DEBUG_TAG,strTemp);
                                    setOutParams(infoTemp,jsonTemp);

                                    Log.e("MET***********",infoTemp+"\n"+jsonTemp.toString());
                                }
                            }
                        } catch (Exception e) {
                            Log.e(DEBUG_TAG,e.getMessage());
                            infoTemp="0：JSON转换错误";
                            Toast.makeText(mContext,infoTemp, Toast.LENGTH_SHORT).show();

                        }finally {
                            setOutParams(infoTemp,jsonTemp);
                        }
                    }
                    if(infoTemp!=null&&infoTemp.substring(0,1).equals("0")){
                        new UIUtil().errorSound(mContext);
                    }
                    setOutParams(infoTemp,jsonTemp);
                    dialog.dismiss();

                }
            });
            //wsUtil.CallWebServiceGetJsonFromSQL(mSQL, myGetHandler);

            // } catch (Exception e) {
            //     Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            // } finally {
            //     dialog.dismiss();
            // }
            //        Looper.loop();
            // }
            //  }).start();
        }
    }


    //设置删除图片

    private void setDrawable() {
        if(length() < 1){
            //imgInable.setBounds(0,0,20,20);
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
        }


            //imgAble.setBounds(0,0,20,20);
        else{
            //imgAble.setBounds(0,0,20,20);
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
        }


    }



    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        Log.e("myedit", "eventX = " + eventX + "; eventY = " + eventY);
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        rect.left = rect.right - 50;
        if(rect.contains(eventX, eventY))
        setText("");
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /*
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
        // 此处为得到焦点时的处理内容
            this.selectAll();
        } else {

            if (listener != null ) {
                listener.OnMyEditTextGetFocusListener();
            }
            if (!TextUtils.isEmpty(getText())) {

                /*

                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.show();
                //new Thread(new Runnable() {
                //    @Override
                //    public void run() {
                //        Looper.prepare();
                        //try {
                            WSUtil.CallWS(null, null, method, activaMap, paramsMap, new WSUtil.WSCallBack() {
                                @Override
                                public void callBack(String result) {
                                    JSONObject objTemp=null;
                                    String info=null;
                                    String strTT = result.substring(0,1);
                                    if(strTT.equals("0")||strTT.equals("1"))
                                    {
                                        Toast.makeText(mContext, result  , Toast.LENGTH_SHORT).show();
                                        info=result;

                                    }else{
                                        try {
                                            JSONObject obj = JSON.parseObject(result);
                                            JSONArray noArr = (JSONArray) JSON.parse(obj.get("Result").toString());

                                            if (noArr.isEmpty()) {
                                                info="0:订单信息错误";
                                                Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();

                                            } else {
                                                String strTemp = "";

                                                for (Object no : noArr) {
                                                    strTemp = no.toString();
                                                    objTemp = JSON.parseObject(strTemp);
                                                    Log.e(DEBUG_TAG,strTemp);
                                                    if(listener !=null){
                                                        listener.OnMyEditTextLostFocusListener(objTemp,info);
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            Log.e(DEBUG_TAG,e.getMessage());
                                            info="0：JSON转换错误";
                                            Toast.makeText(mContext,info, Toast.LENGTH_SHORT).show();

                                        }finally {
                                            listener.OnMyEditTextLostFocusListener(objTemp,info);
                                        }
                                    }
                                    if(info!=null&&info.substring(0,1).equals("0")){
                                        new UIUtil().errorSound(mContext);
                                    }
                                    listener.OnMyEditTextLostFocusListener(objTemp,info);
                                    dialog.dismiss();
                                }
                            });
                            //wsUtil.CallWebServiceGetJsonFromSQL(mSQL, myGetHandler);

                       // } catch (Exception e) {
                       //     Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                       // } finally {
                       //     dialog.dismiss();
                       // }
                //        Looper.loop();
                // }
            //  }).start();
            }
        }
    }
    */


/*
    private Handler myGetHandler = new Handler() {
        @Override

        public void handleMessage(Message msg) {

            Bundle b = msg.getData();

            String result = b.getString("Result");


            String strTT = result.substring(0,5);

            if(strTT.equals("Error"))

            {

                Toast.makeText(mContext, result  , Toast.LENGTH_LONG).show();

            } else {

                try {

                    JSONObject obj = JSON.parseObject(result);

                    JSONArray noArr = (JSONArray) JSON.parse(obj.get("Result").toString());

                    if (noArr.isEmpty()) {

                        Toast.makeText(mContext, "没有找到!", Toast.LENGTH_LONG).show();

                    } else {

                        String strTemp = "";


                        for (Object no : noArr) {

                            strTemp = no.toString();

                            JSONObject objTemp = JSON.parseObject(strTemp);

                            if(listener !=null){

                                listener.OnMyEditTextLostFocusListener(objTemp);

                            }

                        }

                    }

                } catch (Exception e) {

                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }


        }


    };

/**
 * 回调接口
 */
    public interface OnMyEditTextListener{
        /**
         * 得到焦点回调
         */
        void OnMyEditTextGetFocusListener();
        /**
         * 失去焦点回调
         */
        void OnMyEditTextLostFocusListener(JSONObject jsonObject,String info);

    }

    public interface OnMyEditorActionListener{
        void OnMyEditorDoneListener(JSONObject jsonObject,String info);
    }


}
