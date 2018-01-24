package com.thb.ws;

public class WSUserBean {
	private int id;
	private String account;
	private String password;
	
	public String convertString(){
		return "WSUserBean [account="+account+",password="+password+",id="+id+"]";
	}
	
	public WSUserBean(String account, String password){
		super();

		this.account = account;
		this.password = password;
	}
	
	public WSUserBean(){
		
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public String getAccount(){
		return account;
	}
	
	public void setAccount(String account){
		this.account = account;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
}
