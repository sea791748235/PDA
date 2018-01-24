package com.thb.ws;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thb.ui.UIUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sea79 on 2018/1/4.
 */

public class ComWSUtil {

    //DEBUG_TAG
    private static final String DEBUG_TAG = "ComWSUtil:************";

    //WebService三要素
    private static final String URL = "http://192.168.9.91/BaanWebService/BaanWebService.asmx?wsdl";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD = "ReportOrdersCompleted";
    //private static final String MYURL="http://192.168.30.48:10086/WebService.asmx?wsdl";

    //fixed线程池
    private static final ExecutorService executorService=Executors.newFixedThreadPool(1);

    //回调接口返回String
    public interface ComWSCallBack{
        void callBack(String info, JSONObject jsonObject);
    }

    /**
     * @return resultStr 返回String
     */
    public static void callComWS(String url, String nameSpace, final String method, final Context mContext, Map activaMap, Map paramsMap, final ComWSCallBack comWSCallBack  ) {

        Log.e(DEBUG_TAG,method+'\n'+activaMap.toString()+'\n'+paramsMap.toString());

        if(url==null){
            url=URL;
        }
        if(nameSpace==null){
            nameSpace=NAMESPACE;
        }
        //HttpTransportSE(url,timeout)对象传输URL,设置响应时间,响应时间默认30s
        final HttpTransportSE ht = new HttpTransportSE(url);
        ht.debug = true;

        //生成调用Webservice方法的SOAP请求信息。
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;

        //2.SoapObject对象指定命名空间及方法名
        SoapObject request = new SoapObject(nameSpace, method);

        //ps1:soap header调用权限验证
        //set headerout

        if(!(activaMap==null)){

            Element[] header=new Element[1];
            header[0]=new Element().createElement(nameSpace,"Activation");

            Element username=new Element().createElement(nameSpace,"username");
            username.addChild(Node.TEXT,activaMap.get("username"));
            header[0].addChild(Node.ELEMENT,username);

            Element password=new Element().createElement(nameSpace,"password");
            password.addChild(Node.TEXT,activaMap.get("password"));
            header[0].addChild(Node.ELEMENT,password);

            Element company=new Element().createElement(nameSpace,"company");
            company.addChild(Node.TEXT,activaMap.get("company"));
            header[0].addChild(Node.ELEMENT,company);

            envelope.headerOut=header;
        }

        //3.设置传入参数
        // 传递复杂参数与基本类型参数方法不同
        if(method==METHOD&&!(paramsMap == null)){
            ReportOrdersCompletedMD md=new ReportOrdersCompletedMD();
            md.setProperty(0,paramsMap.get("Unique"));
            md.setProperty(1,paramsMap.get("ProductionOrder"));
            md.setProperty(2,paramsMap.get("QuantityToDeliver"));
            md.setProperty(3,paramsMap.get("LotCode"));

            PropertyInfo pi=new PropertyInfo();
            pi.setName("entity");
            pi.setValue(md);
            pi.setType(md.getClass());

            request.addProperty(pi);
            envelope.bodyOut = request;
            envelope.addMapping(nameSpace,"ReportOrdersCompletedMD",ReportOrdersCompletedMD.class);
        }
        if(method!=METHOD&&!(paramsMap == null)){

            Iterator iter = paramsMap.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry localEntry = (HashMap.Entry) iter.next();
                request.addProperty((String) localEntry.getKey(), localEntry.getValue());
            }
            envelope.bodyOut = request;
        }

        final Handler mHandler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                String info=null;
                JSONObject jsonObject=null;

                String result=(String)msg.obj;
                String strTT = result.substring(0,1);
                if(strTT.equals("0")||strTT.equals("1"))
                {
                    //Toast.makeText(mContext, resultStr  , Toast.LENGTH_SHORT).show();
                    info=result;
                    Log.e("MET***********",info);
                }else{

                    try {
                        JSONObject obj = JSON.parseObject(result);
                        JSONArray noArr = (JSONArray) JSON.parse(obj.get("Result").toString());

                        if (noArr.isEmpty()) {
                            info="0:订单信息错误";
                            //Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();

                            Log.e("MET***********",info);
                        } else {
                            String strTemp = "";

                            for (Object no : noArr) {
                                strTemp = no.toString();
                                jsonObject = JSON.parseObject(strTemp);
                                Log.e(DEBUG_TAG,strTemp);

                                Log.e("MET***********",info+"\n"+jsonObject.toString());
                            }
                        }
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG,e.getMessage());
                        info="0：JSON转换错误";
                        //Toast.makeText(mContext,info, Toast.LENGTH_SHORT).show();

                    }
                }
                //setOutParams(info,jsonTemp);
                //pg.dismiss();
                comWSCallBack.callBack(info,jsonObject);
            }
        };

        //使用call方法调用WebService
        Log.e(DEBUG_TAG,request.toString());

        executorService.submit(new Runnable() {

            @Override
            public void run() {


                String resultStr=null;

                try{
                    ht.call(null, envelope);
                    //返回String
                    if (envelope.getResponse() != null) {
                        SoapObject result= (SoapObject) envelope.bodyIn;
                        resultStr = result.getPropertyAsString(method+"Result");

                        Log.e("---收到的回复---", resultStr);
                    }
                }catch (Exception e) {
                    resultStr="0:网络错误";
                    Log.e("---错误---", e.getMessage());
                }finally {
                    mHandler.sendMessage(mHandler.obtainMessage(0,resultStr));
                }
            }
        });
    }

}
