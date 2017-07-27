package com.hector.netty.service;

import com.hector.netty.data.Request;
import com.hector.netty.data.Response;

public interface Service {
	public Response doService(Request request);
}
