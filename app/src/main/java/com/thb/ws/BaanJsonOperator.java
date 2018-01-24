package com.thb.ws;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Json数据解析类
 * Created by sea79 on 2017/11/3.
 */

public class BaanJsonOperator {

    /**
     *
     * 解析带数据头List的Json,转化为List<bean>
     *
     */

    public <T> ArrayList<T> HListJson2ListBean(String BaanJson,Class<T> cls){
        ArrayList<T> list=new ArrayList<>();
        try{
            JsonObject jsonObject=new JsonParser().parse(BaanJson).getAsJsonObject();
            JsonArray jsonArray=jsonObject.getAsJsonArray("Result");
            Gson gson=new Gson();
            for(JsonElement T:jsonArray){
                T t=gson.fromJson(T,cls);
                list.add(t);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //json转换java Bean
    public static <T> T Json2Bean(String jsonString,Class<T> cls){
        T t =null;
        try{
            Gson gson=new Gson();
            t = gson.fromJson(jsonString, cls);
        }catch(Exception e){
            e.printStackTrace();
        }
        return t;
    }

    //Bean 转换 Json
    public String Bean2Json(Object bean){
        Gson gson=new Gson();
        String JsonStr=gson.toJson(bean);
        return JsonStr;
    }

    //String 转换 Json
    public static <T> String Str2Json(Object o){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(o);
        return jsonStr;
    }

    //Json 转换 List
    public static <T> List<T> Json2List(String jsonString,Type type){
        List<T> list = new ArrayList<T>();
        try{
            Gson gson = new Gson();
            list = gson.fromJson(jsonString,type);
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Json转换Map
     */
    public static HashMap<String,String> Json2HashMap(String jsonString){
        HashMap<String,String> hashMap=new HashMap<String,String>();
        try{
            Gson gson=new Gson();
            hashMap=gson.fromJson(jsonString,new TypeToken<HashMap<String,String>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
        }
        return hashMap;
    }
}
