package com.hector.netty.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyFuture {
	private Sync sync ;
	
	private volatile Object result;
	
	public MyFuture() {
		this.sync = new Sync();
	}

	
	public Object get(){
		sync.acquire(-1);
		return result;
	}
	
	public Object get(long timeout, TimeUnit unit) throws InterruptedException{
		boolean success = sync.tryAcquireNanos(-1,  unit.toNanos(timeout));
		if(success){
			return result;
		}else{
			throw new RuntimeException("Timeout exception. Request id: " + this.result
            + ". Request class name: " + this.result
            + ". Request method: " + this.result);
		}
	}
	
	/**
	 * response 的可以情况有2种 1 ok正常返回 2错误的返回信息
	 * @param response
	 */
	public static void received(Object response){
		
	}

	private static class Sync extends AbstractQueuedSynchronizer{
		
		private final static int NEW = 0;
		private static final int COMPLETING = 1;
		private static final int NORMAL = 2;
		private static final int EXCEPTIONAL = 3;
		
		
		
		@Override
		protected boolean tryAcquire(int arg) {
			return  getState()== NORMAL? true:false;
		}

		@Override
		protected boolean tryRelease(int arg) {
			if(compareAndSetState(NEW, NORMAL)){
				return true;
			}
			return false;
		}
		
	}
}
