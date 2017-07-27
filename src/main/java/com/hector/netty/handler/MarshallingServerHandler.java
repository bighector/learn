package com.hector.netty.handler;

import java.util.concurrent.ExecutorService;

import com.hector.netty.data.Request;
import com.hector.netty.data.Response;
import com.hector.netty.service.ServiceSupport;
import com.hector.netty.task.TaskRunnable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MarshallingServerHandler extends ChannelInboundHandlerAdapter {
	//private ExecutorService executor ;
	private ServiceSupport support;

	
	public MarshallingServerHandler(ServiceSupport support) {
		this.support = support;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof Request){
			Request q = (Request) msg;
			System.out.println(q);
//			Response resp = doRequest(q);
//			ctx.writeAndFlush(resp);
			TaskRunnable task = new TaskRunnable(ctx.channel(),support,q);
			
			TaskRunnable.submit(task);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(cause);
		ctx.close();
	}
	
	
	private Response doRequest(Request request){
		return null;
	}
}
