package com.thb.qr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rscja.utility.StringUtility;
import com.thb.ui.UIUtil;
import com.thb.ws.WSUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by sea79 on 2017/11/20.
 */

public class QRUtil {
    //扫码api
    public Barcode2DWithSoft mReader;

    public Context mContext;
    public Activity mActivity;

    public int successCount=0;
    public int failCount=0;
    public int totalCount=0;

    public String qrResult;

    public String getQrResult(){
        return qrResult;
    }

    public void setQrResult(String qrResult){
        this.qrResult=qrResult;
    }
    /*
    public void setQR(Context mContext,Barcode2DWithSoft mReader, boolean isContinue,int interval){
        this.mContext=mContext;
        this.mReader=mReader;
        this.isContinue=isContinue;
        this.interval=interval;
        QRinit();
    }
    */
    public QRUtil(){

    }

    public QRUtil(Context mContext){
        this.mContext=mContext;
    }

    public void QRinit(){
        try{
            mReader=Barcode2DWithSoft.getInstance();
            Toast.makeText(mContext,"扫描功能已就绪",Toast.LENGTH_SHORT).show();
            new InitTask().execute();

        }catch (Exception e){
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void doScan(){
        mReader.scan();
        mReader.setScanCallback(mScanCallback);
        setQrResult(qrResult);
    }

    public void stopScan(){
        if(mReader!=null){
            mReader.close();
        }
    }

    public Barcode2DWithSoft.ScanCallback mScanCallback=new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] data) {

            if (length < 1) {
                failCount+=1;
                qrResult="0:扫描失败";
                return;
            }

            mReader.stopScan();
            qrResult=new String(data).trim();
            Log.e("QRUtil**********",qrResult);

        }
    };

    public class InitTask extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog myDialog;

        @Override
        protected Boolean doInBackground(String...params){
            boolean result=false;

            if(mReader!=null){
                result=mReader.open(mContext);
                if(result){
                    mReader.setParameter(324,1);
                    mReader.setParameter(300,0);
                    mReader.setParameter(361,0);
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);
            myDialog.cancel();

            if(!result){
                Toast.makeText(mContext,"初始化失败",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            myDialog=new ProgressDialog(mContext);
            myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            myDialog.setMessage("初始化中...");
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.show();
        }
    }
    /*
    public void setSingleQR(Context mContext,Barcode2DWithSoft mReader){
        this.mContext=mContext;
        this.mReader=mReader;
        QRinit();
    }

    public String getQRStr(){

        Log.e("QRUtil:**************",barCode);
        return barCode;
    }

    public ArrayList<String> getQRList(){
        Log.e("**************",barCodeList.toString());
        return barCodeList;
    }



    public Barcode2DWithSoft.ScanCallback mScanCallback=new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] data) {

            String strData="";

            if (length < 1) {
                failCount+=1;
                strData = "扫描失败" + "\n";
                resultStr+=strData;
                return;
            }

            mReader.stopScan();
            barCode = new String(data).trim();

            k++;
            barCodeList.add(barCode);
            strData="第"+k+"个："+barCode+"\n";
            resultStr+=strData;
            Log.e("!!!!!!!!!!!",resultStr);
        }
    };

    public String doDecode(){

        if(mReader != null){
            mReader.setScanCallback(mScanCallback);
        }

        if(isThreadStop){

            if(isContinue=true){
                buttonStr="停止";
                isThreadStop=false;
            }

            thread=new DecodeThread(isContinue,interval);
            thread.start();

        }else{
            buttonStr="扫描";
            isThreadStop=true;
        }

        return buttonStr;
    }

    public class DecodeThread extends Thread{
        private boolean isContinue=false;
        private long sleepTime=1000;

        public DecodeThread(boolean isContinue,int sleep){
            this.isContinue=isContinue;
            this.sleepTime=sleep;
        }

        @Override
        public void run(){
            super.run();

            do{
                mReader.scan();

                if(isContinue){
                    try{
                        Thread.sleep(sleepTime);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }while (isContinue&&!isThreadStop);
        }
    }
    */

    /*
    public void scrollToBottom(final View scroll,final View inner){
        Handler mHandler=new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(scroll==null||inner==null){
                    return;
                }
                int offset=inner.getMeasuredHeight()-scroll.getHeight();
                if(offset<0){
                    offset=0;
                }

                scroll.scrollTo(0,offset);
            }
        });
    }

    public void clearQR(){
        barCodeList.clear();
    }

    public void startQR(){
        mReader.scan();
        mReader.setScanCallback(mScanCallback);
        //mReader.close();
    }

    public void stopQR(){
        mReader.stopScan();
        //mReader.close();
    }
    */



}
