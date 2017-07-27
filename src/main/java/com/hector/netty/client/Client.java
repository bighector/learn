package com.hector.netty.client;

import com.hector.netty.handler.MarshallingClientHandler;
import com.hector.netty.handler.marshalling.MarshallingCodeCFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	private Bootstrap b;
	private EventLoopGroup group = new NioEventLoopGroup();
	
	public void init(){
		b = new Bootstrap();
		b.group(group);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(initChannel());
	}
	
	public void start(){
		
	}
	
	public void close(){
		group.shutdownGracefully();
	}
	
	public ChannelFuture  connect(String address, int port){
		ChannelFuture future = b.connect(address, port);
		try {
			future.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return future;
	}
	
	
	public static void main(String[] args) {
		Client c = new Client();
		c.init();
		ChannelFuture channelFuture = c.connect("127.0.0.1", 8888);
		try {
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			c.close();
		}
	}
	
	 
	private ChannelInitializer initChannel(){
		return new ChannelInitializer<SocketChannel>(){

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("marshallingDecoder",MarshallingCodeCFactory.buildMarshallingDecoder());
				ch.pipeline().addLast("marshallingEncoder",MarshallingCodeCFactory.buildMarshallingEncoder());
				ch.pipeline().addLast("serverHandler",new MarshallingClientHandler());
			}
			
		};
	}
	
	
}
