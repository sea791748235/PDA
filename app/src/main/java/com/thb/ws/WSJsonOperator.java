package com.thb.ws;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WSJsonOperator {
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
    
    public static <T> String toJson(Object o){
    	Gson gson = new Gson();
    	String jsonStr = gson.toJson(o);
    	return jsonStr;
    }
    
    public static <T> List<T> json2List(String jsonString,Type type){
    	List<T> list = new ArrayList<T>();
    	try{
    		Gson gson = new Gson();
    		list = gson.fromJson(jsonString, type);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return list;
    }
}
