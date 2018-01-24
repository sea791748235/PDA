package com.thb.ws;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sea79 on 2017/10/20.
 */

public class WSSoapService {
    private final static String url="http://192.168.30.49:18081/login?wsdl";
    private String namespace="http://wsdemo.com/";
    private String methodName="";
    private Class<?> cls;
    private HashMap<String,String> paramsMap;
    private String param;


    public void setNamespace(String namespace){
        this.namespace=namespace;
    }

    public void setMethodName(String methodName){
        this.methodName=methodName;
    }

    public String invoke() throws XmlPullParserException,IOException{
        HttpTransportSE httpTransportSE=new HttpTransportSE(url);
        httpTransportSE.debug=true;

        SoapObject requestObject=new SoapObject(namespace,methodName);

        Iterator it=paramsMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry=(Map.Entry)it.next();
            String paramName=entry.getKey().toString();
            String value=entry.getValue().toString();
            requestObject.addProperty(paramName,value);
        }
        SoapSerializationEnvelope soapSerializationEnvelope=
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.setOutputSoapObject(requestObject);
        soapSerializationEnvelope.dotNet=false;
        String resultJsonStr="";
        httpTransportSE.call(null,soapSerializationEnvelope);
        if(soapSerializationEnvelope.getResponse()!=null){
            SoapObject result=(SoapObject)soapSerializationEnvelope.bodyIn;
            resultJsonStr=result.getProperty("return").toString();
        }
        return resultJsonStr;
    }

    public void setParams(String className){
        try{
            cls=Class.forName(className);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setParams(HashMap<String,String> paramsMap){
        this.paramsMap=paramsMap;
    }

}
