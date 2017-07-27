package com.hector.netty.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hector.netty.data.Request;
import com.hector.netty.data.Response;
import com.hector.netty.service.ServiceSupport;

import io.netty.channel.Channel;

public class TaskRunnable implements Runnable {
	private Channel channel;
	private ServiceSupport serviceSupport;
	private Request request;
	
	private final static ExecutorService executor = Executors.newCachedThreadPool();
	
	public TaskRunnable(Channel channel, ServiceSupport serviceSupport, Request request) {
		this.channel = channel;
		this.serviceSupport = serviceSupport;
		this.request = request;
	}



	public void run() {
		Response sesponse = serviceSupport.doService(request);
		channel.writeAndFlush(sesponse);
	}

	public static void submit(Runnable runnable){
		executor.submit(runnable);
	}
}
