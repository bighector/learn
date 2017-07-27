package com.hector.netty.data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class Request implements Serializable {
	private long id;
	private String name;
	private String message;
	private static final AtomicLong sequence = new AtomicLong(0);
	
	private Request(){
		this(null,null);
	}
	
	private Request(String name, String message) {
		this.id = sequence.incrementAndGet();
		this.name = name;
		this.message = message;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public static Request buildRequest(){
		return new Request();
	}
	
	@Override
	public String toString() {
		return "id = " +getId() +",name = " +getName() +",message = "+ getMessage();
	}
}
