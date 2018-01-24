package com.thb.ui;

import java.io.Serializable;

/**
 * Created by sea79 on 2017/12/6.
 */

public class ElistGroupBean implements Serializable {

    private String g1;
    private String g2;

    public ElistGroupBean(String g1,String g2){
        this.g1=g1;
        this.g2=g2;
    }

    public String getG1(){
        return g1;
    }

    public void setG1(String g1){
        this.g1=g1;
    }

    public String getG2(){
        return g2;
    }

    public void setG2(String g2){
        this.g2=g2;
    }
}
