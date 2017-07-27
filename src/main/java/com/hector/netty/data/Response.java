package com.hector.netty.data;

import java.io.Serializable;

public class Response implements Serializable {
	private long id;
	private int status;
	private Object result;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "id = " +getId() +",status = " +getStatus() +",result = "+ getResult();
	}
	
}
