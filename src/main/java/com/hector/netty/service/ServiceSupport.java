package com.hector.netty.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hector.netty.data.Request;
import com.hector.netty.data.Response;

/**
 * singleton
 * @author Administrator
 *
 */
public class ServiceSupport implements Service{
	private Service service;
	//
	private Semaphore semaphore;
	private ExecutorService executor ;
	
	
	
	public ServiceSupport(Service service, Semaphore semaphore) {
		this.service = service;
		this.semaphore = semaphore;
		this.executor = Executors.newCachedThreadPool();
	}

	public Response doService(final Request request) {
		boolean success = semaphore.tryAcquire(1);
		System.out.println("success = " +success);
		if(success){
			try{
				//Response resp = service.doService(request);
				FutureTask<Response> future = new FutureTask<Response>(new Callable<Response>() {

					public Response call() throws Exception {
						Response r = service.doService(request);
						System.out.println("r = " +r);
						return r;
					}
					
				});
				executor.submit(future);
				
				Response resp =  new Response();
				try {
					resp = (Response)future.get(5, TimeUnit.SECONDS);
					//resp = (Response)future.get();
					System.out.println("Future resp = " +resp);
				} catch (InterruptedException e) {
					e.printStackTrace();
					resp.setId(request.getId());
					resp.setResult(e.getMessage());
					resp.setStatus(-1);
				} catch (ExecutionException e) {
					e.printStackTrace();
					resp.setId(request.getId());
					resp.setResult(e.getMessage());
					resp.setStatus(-1);
				} catch (TimeoutException e) {
					e.printStackTrace();
					resp.setId(request.getId());
					resp.setResult("Service Timeout");
					resp.setStatus(4);
					
				}
				return resp;
			}finally{
				semaphore.release(1);
			}
		}else{
			Response resp = new Response();
			resp.setId(request.getId());
			resp.setStatus(2);
			resp.setResult("·þÎñÆ÷ÒÑÂú£¡");
			return  resp;
		}
	}
	
	
}
