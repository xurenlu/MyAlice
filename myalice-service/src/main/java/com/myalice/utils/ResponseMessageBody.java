package com.myalice.utils;

public class ResponseMessageBody {
	
	protected String msg ; 
	
	protected boolean suc ;

	public ResponseMessageBody() {
	}

	public ResponseMessageBody(String msg, boolean suc) {
		this.msg = msg;
		this.suc = suc;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuc() {
		return suc;
	}

	public void setSuc(boolean suc) {
		this.suc = suc;
	}
	
}