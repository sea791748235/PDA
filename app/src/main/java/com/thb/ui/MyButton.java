package com.thb.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.Toast;

import com.thb.app.R;
import com.thb.ws.BaanSoapService;

import java.util.HashMap;

/**
 * Created by sea79 on 2017/10/30.
 */

public class MyButton extends AppCompatImageButton{

    private static final int DURATION=15;
    private static int GAP=10;
    private static int TIMEOUT;

    private int viewW,viewH;
    private int pointX,pointY;
    private int maxRadio;
    private int shaderRadio;

    private Paint bottomPaint,colorPaint;

    private boolean isPushButton;

    private int eventX,eventY;
    private long downTime=0;

    public MyButton(Context context,AttributeSet attrs){
        super(context,attrs);
        initPaint();
        TIMEOUT= ViewConfiguration.getLongPressTimeout();
    }

    private void initPaint(){
        colorPaint=new Paint();
        bottomPaint=new Paint();
        colorPaint.setColor(getResources().getColor(R.color.blue));
        bottomPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void countMaxRadio(){
        if(viewW>viewH){
            if(eventX<viewW/2){
                maxRadio=viewW-eventX;
            }else {
                maxRadio=viewW/2+eventX;
            }
        }else {
            if(eventY<viewH/2){
                maxRadio=viewH-eventY;
            }else {
                maxRadio=viewW/2+eventY;
            }
        }
    }

    private void clear(){
        downTime=0;
        GAP=10;
        isPushButton=false;
        shaderRadio=0;
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(downTime==0)downTime= SystemClock.elapsedRealtime();
                eventX=(int) event.getX();
                eventY=(int)event.getY();

                countMaxRadio();
                isPushButton=true;
                postInvalidateDelayed(DURATION);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                if(SystemClock.elapsedRealtime()-downTime<TIMEOUT){
                    GAP=30;
                    postInvalidate();
                }else {
                    clear();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas){
        super.dispatchDraw(canvas);
        if(!isPushButton)return;
        canvas.drawRect(pointX,pointY,pointX+viewW,pointY+viewH,bottomPaint);
        canvas.save();
        canvas.clipRect(pointX,pointY,pointX+viewW,pointY+viewH);
        canvas.drawCircle(eventX,eventY,shaderRadio,colorPaint);
        canvas.restore();
        if(shaderRadio<maxRadio){
            postInvalidateDelayed(DURATION,pointX,pointY,pointX+viewW,pointY+viewH);
            shaderRadio+=GAP;
        }else {
            clear();
        }
    }

    @Override
    protected void onSizeChanged(int w,int h,int ow,int oh){
        super.onSizeChanged(w,h,ow,oh);
        this.viewW=w;
        this.viewH=h;
    }
}
