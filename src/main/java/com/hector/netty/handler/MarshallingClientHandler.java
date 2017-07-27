package com.hector.netty.handler;

import com.hector.netty.data.Request;
import com.hector.netty.data.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MarshallingClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i = 0;i<10;i ++){
			Request q = Request.buildRequest();
			q.setName("name"+i);
			q.setMessage("message"+i);
			ctx.channel().writeAndFlush(q);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive :" + ctx.channel() );
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof Response){
			Response resq = (Response)msg;
			System.out.println("client resp = " +resq);
			
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(cause);
		ctx.close();
	}
	
}
