package com.hector.netty.server;

import java.util.concurrent.Semaphore;

import com.hector.netty.handler.MarshallingServerHandler;
import com.hector.netty.handler.marshalling.MarshallingCodeCFactory;
import com.hector.netty.service.DefaultService;
import com.hector.netty.service.Service;
import com.hector.netty.service.ServiceSupport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	private EventLoopGroup bossGroup = new NioEventLoopGroup(); 
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
    private ServerBootstrap b =null;
    private  ServiceSupport serviceSupport = null ;
    
	public void init(){
		b = new ServerBootstrap();
		b.group(bossGroup, workerGroup);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(initChnnel());
		b.option(ChannelOption.SO_BACKLOG, 128);
		b.childOption(ChannelOption.SO_KEEPALIVE, true);
		
		Service service = new DefaultService();
		Semaphore semaphore = new Semaphore(2);
		serviceSupport = new ServiceSupport(service, semaphore);
	}
	
	public ChannelFuture start(int port){
		ChannelFuture f = null;
		try {
			 f = b.bind(port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	public void close(){
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	
	
	
	private ChannelInitializer initChnnel(){
		return new ChannelInitializer(){

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast("marshallingDecoder",MarshallingCodeCFactory.buildMarshallingDecoder());
				ch.pipeline().addLast("marshallingEncoder",MarshallingCodeCFactory.buildMarshallingEncoder());
				ch.pipeline().addLast("serverHandler",new MarshallingServerHandler(getServiceSupport()));
			}
			
		};
	}
	
	
	
	public ServiceSupport getServiceSupport() {
		return serviceSupport;
	}

	public static void main(String[] args) {
		Server server= new Server();
		server.init();
		ChannelFuture f = server.start(8888);
		try {
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			server.close();
		}
		
	}
}
