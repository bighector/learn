package com.hector.netty.service;

import java.util.concurrent.CountDownLatch;

import com.hector.netty.data.Request;
import com.hector.netty.data.Response;

public class DefaultService implements Service {
	//private CountDownLatch latch;
	
	public Response doService(Request request) {
		Response resp= new Response();
		try{
			System.out.println("DefaultService.request = " +request);
			resp = doWork(request);
			System.out.println("DefaultService.resp = " +resp);
			return resp;
		}catch(Exception e){
			e.printStackTrace();
			resp.setId(request.getId());
			resp.setStatus(3);
			resp.setResult(e.getMessage());
			return resp;
		}finally{
//			latch.countDown();
		}
	}

	private Response doWork(Request request) throws  Exception{
		Response resp= new Response();
		resp.setId(request.getId());
		resp.setStatus(1);
		resp.setResult("ok");
		Thread.currentThread().sleep(6000);
		return resp;
	}
}
