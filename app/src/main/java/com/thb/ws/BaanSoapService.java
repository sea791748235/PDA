package com.thb.ws;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 难点：
 * 1，头文件验证
 * 解决方法：Element添加XML节点
 * 2，复杂类型传参
 *解决方法：bean实现KvmSerializable接口，envelope添加mapping
 * 3，HttpTransportSE响应时间不能太短！！！！
 * Created by sea79 on 2017/11/1.
 */

public class BaanSoapService {
    //DEBUG_TAG
    final static String DEBUG_TAG = "BaanSoapService";

    //WebService三要素
    final static String URL = "http://192.168.9.91/BaanWebService/BaanWebService.asmx";
    final static String NAMESPACE = "http://tempuri.org/";
    final static String METHOD1 = "ReportOrdersCompleted";

    //set 所有参数
    private HashMap<String,String> paramsMap=new HashMap<>();
    private HashMap<String,String> activaMap=new HashMap<>();
    private String methodStr=null;

    public void setAll(HashMap<String,String> activaMap,HashMap<String,String> paramsMap,String methodStr){
        this.activaMap=activaMap;
        this.paramsMap=paramsMap;
        this.methodStr=methodStr;
    }
    /**
     * @return resultStr 返回String
     */
    public String CallBaanWS() {
        //接受参数
        String resultStr= "";

        //1.HttpTransportSE对象传输URL,设置响应时间,响应时间不能太小
        HttpTransportSE ht = new HttpTransportSE(URL);
        ht.debug = true;
        //ps1:soap header调用权限验证

        Element[] header=new Element[1];
        header[0]=new Element().createElement(NAMESPACE,"Activation");

        Element username=new Element().createElement(NAMESPACE,"username");
        username.addChild(Node.TEXT,activaMap.get("username"));
        header[0].addChild(Node.ELEMENT,username);

        Element password=new Element().createElement(NAMESPACE,"password");
        password.addChild(Node.TEXT,activaMap.get("password"));
        header[0].addChild(Node.ELEMENT,password);

        Element company=new Element().createElement(NAMESPACE,"company");
        company.addChild(Node.TEXT,activaMap.get("company"));
        header[0].addChild(Node.ELEMENT,company);

        //2.SoapObject对象指定命名空间及方法名
        SoapObject request = new SoapObject(NAMESPACE, methodStr);
        //4.生成调用Webservice方法的SOAP请求信息。
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        //set headerout
        envelope.headerOut=header;
        //3.设置传入参数
        // 传递复杂参数与基本类型参数方法不同
        if(methodStr==METHOD1){
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
            envelope.addMapping(NAMESPACE,"ReportOrdersCompletedMD",ReportOrdersCompletedMD.class);
        }else {
            Iterator iter = paramsMap.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry localEntry = (HashMap.Entry) iter.next();
                request.addProperty((String) localEntry.getKey(), localEntry.getValue());
            }
            envelope.bodyOut = request;
        }




        //5.使用call方法调用WebService
        Log.e("&&&&&&&&&&&&&&&&&&",request.toString());

        try{
            ht.call(null, envelope);
            //6.返回String
            if (envelope.getResponse() != null) {
                SoapObject result= (SoapObject) envelope.bodyIn;
                resultStr = result.getPropertyAsString(methodStr+"Result");
                Log.e("---收到的回复---", resultStr.toString());
                }
            }catch (IOException e) {
            resultStr = e.getMessage();
            //Log.e("---IO错误---", e.getMessage());
        }catch (XmlPullParserException e) {
            resultStr=e.getMessage();
            //Log.e("---XML解析错误---", e.getMessage());
        }catch (Exception e){
            resultStr=e.getMessage();
            //Log.e("---exception---",e.getMessage());
        }
        return resultStr;
     }
}

