package com.thb.ws;

/**
 * Created by sea79 on 2017/10/26.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestJsonOperator {

    //json转换java Bean
    public static <T> T json2Bean(String jsonString,Class<T> cls){
        T t =null;
        try{
            Gson gson=new Gson();
            t = gson.fromJson(jsonString, cls);
        }catch(Exception e){
            e.printStackTrace();
        }
        return t;
    }

    //String 转换 Json
    public static <T> String toJson(Object o){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(o);
        return jsonStr;
    }

    //Json 转换 List
    public static <T> List<T> json2List(String jsonString,Type type){
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