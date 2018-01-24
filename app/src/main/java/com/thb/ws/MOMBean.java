package com.thb.ws;

/**
 * Created by sea79 on 2017/12/19.
 */

public class MOMBean {

    private String T$PDNO;
    private String T$MITM;
    private String T$PONO;
    private String T$SITM;
    private String T$QUES;

    public MOMBean(){

    }

    public MOMBean(String T$PDNO,String T$MITM,String T$PONO,String T$SITM,String T$QUES){

        this.T$PDNO=T$PDNO;
        this.T$MITM=T$MITM;
        this.T$PONO=T$PONO;
        this.T$SITM=T$SITM;
        this.T$QUES=T$QUES;

    }

    public String getT$PDNO(){
        return T$PDNO;
    }

    public void setT$PDNO(String T$PDNO){
        this.T$PDNO=T$PDNO;
    }

    public String getT$MITM(){
        return T$MITM;
    }

    public void setT$MITM(String T$MITM){
        this.T$MITM=T$MITM;
    }

    public String getT$PONO(){
        return T$PONO;
    }

    public void setT$PONO(String T$PONO){
        this.T$PONO=T$PONO;
    }

    public String getT$SITM(){
        return T$SITM;
    }

    public void setT$SITM(String T$SITM){
        this.T$SITM=T$SITM;
    }

    public String getT$QUES(){
        return T$QUES;
    }

    public void setT$QUES(String T$QUES){
        this.T$QUES=T$QUES;
    }
}
