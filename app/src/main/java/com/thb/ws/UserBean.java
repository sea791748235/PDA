package com.thb.ws;

/**
 * Created by sea79 on 2017/11/20.
 */

public class UserBean {

    private String username;
    private String password;
    private String company;

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company){
        this.company=company;
    }
}
