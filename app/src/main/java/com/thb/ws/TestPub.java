package com.thb.ws;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sea79 on 2017/10/30.
 */

public class TestPub {
    private final String DEBUG_TAG="SoapService";
    /**
     * WebService 三要素
     */
    static String METHOD_NAME="CallDllWithJson";
    static String URL="http://192.168.30.49:18085/SoapInJson.asmx?wsdl";
    static String NAMESPACE="http://test.com/";

    public String CallSoapService(String strMethod_name, Map<String,String> localObject){
        String strReturn="";
        if(strMethod_name==""){
            strMethod_name=METHOD_NAME;
        }

        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);

        try{
            //1.指定WebService命名空间和调用的方法名
            SoapObject request=new SoapObject(NAMESPACE,strMethod_name);

            //2.设置调用方法的参数值，若无参数可略去
            Iterator iterator=localObject.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry localEntry=(Map.Entry)iterator.next();
                request.addProperty((String)localEntry.getKey(),localEntry.getValue());
            }

            //3.生成调用WebService方法的SOAP请求信息
            envelope.bodyOut=request;
            HttpTransportSE ht=new HttpTransportSE(URL);
            // C#程序需附加
            envelope.dotNet=true;

            //4.call方法调用WebService
                ht.call(null,envelope);
                final SoapPrimitive result=(SoapPrimitive)envelope.getResponse();
                if(request!=null){
                    Log.d("========收到的回复=========",result.toString());
                    return result.toString();
            }
        }catch (IOException e){
            Log.d("========IO错误==========",e.getMessage());
            strReturn="Error:IO错误";
        }catch (XmlPullParserException e){
            Log.d("=======XML解析错误========",e.getMessage());
            strReturn="Error:XML解析错误";
        }
        return  strReturn;
    }

    public void CallSoapPostJson(String dllName, String functionName, String inParam, Handler myHandler){
        String strReturn="";
        if(functionName==""){
            functionName="SoapService.invoke";
        }

        Map<String,String> map=new HashMap<String, String>();


        strReturn=CallSoapService("DllWithJson",map);

        Message msg=new Message();
        Bundle b=new Bundle();
        b.putString("Result",strReturn);
        msg.setData(b);
        myHandler.sendMessage(msg);
    }

    public void CallSoapGetJsonSQL(String strSQL,Handler myHandler){
        String strReturn="";
        Map<String,String> map=new HashMap<String,String>();
        map.put("sql",strSQL);

        strReturn=CallSoapService("GetJsonSQL",map);

        Message msg=new Message();
        Bundle b=new Bundle();
        b.putString("Result",strReturn);
        msg.setData(b);
        myHandler.sendMessage(msg);

    }

}
