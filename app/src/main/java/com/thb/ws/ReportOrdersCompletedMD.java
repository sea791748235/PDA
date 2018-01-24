package com.thb.ws;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * SQL EntityBean
 * Created by sea79 on 2017/11/8.
 */

public class ReportOrdersCompletedMD implements KvmSerializable{
    private String Unique;
    private String ProductionOrder;
    private String QuantityToDeliver;
    private String LotCode;

    public ReportOrdersCompletedMD(String Unique,String ProductionOrder,String QuantityToDeliver,String LotCode){
        this.Unique=Unique;
        this.ProductionOrder=ProductionOrder;
        this.QuantityToDeliver=QuantityToDeliver;
        this.LotCode=LotCode;
    }

    public ReportOrdersCompletedMD(){

    }

    public String getUnique(){
        return Unique;
    }

    public void setUnique(String Unique){
        this.Unique=Unique;
    }

    public String getProductionOrder(){
        return ProductionOrder;
    }

    public void setProductionOrder(String ProductionOrder){
        this.ProductionOrder=ProductionOrder;
    }

    public String getQuantityToDeliver(){
        return QuantityToDeliver;
    }

    public void setQuantityToDeliver(String QuantityToDeliver){
        this.QuantityToDeliver=QuantityToDeliver;
    }

    public String getLotCode(){
        return LotCode;
    }

    public void setLotCode(String LotCode){
        this.LotCode=LotCode;
    }

    @Override
    public Object getProperty(int arg0){
        switch (arg0){
            case 0:
                return Unique;
            case 1:
                return ProductionOrder;
            case 2:
                return QuantityToDeliver;
            case 3:
                return LotCode;
            default:
                break;
        }
        return null;
    }

    @Override
    public void setProperty(int arg0,Object arg1){
        switch (arg0){
            case 0:
                Unique=arg1.toString();
                break;
            case 1:
                ProductionOrder=arg1.toString();
                break;
            case 2:
                QuantityToDeliver=arg1.toString();
                break;
            case 3:
                LotCode=arg1.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int agr0, Hashtable arg1,PropertyInfo arg2){

        switch (agr0){
            case 0:
                arg2.type=PropertyInfo.STRING_CLASS;
                arg2.name="Unique";
                break;
            case 1:
                arg2.type=PropertyInfo.STRING_CLASS;
                arg2.name="ProductionOrder";
                break;
            case 2:
                arg2.type=PropertyInfo.STRING_CLASS;
                arg2.name="QuantityToDeliver";
                break;
            case 3:
                arg2.type=PropertyInfo.STRING_CLASS;
                arg2.name="LotCode";
                break;
            default:
                break;
        }
    }

    @Override
    public int getPropertyCount(){
        return 4;
    }
}