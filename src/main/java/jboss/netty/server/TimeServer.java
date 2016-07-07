package jboss.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	public void bind(int port){
		//接受客户端连接NIO线程
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		//处理socketchannel的网络读写NIO线程
		NioEventLoopGroup ioGroup=new NioEventLoopGroup();
		try {
		ServerBootstrap b=new ServerBootstrap();//启动NIO服务端的辅助启动类
		b.group(workGroup, ioGroup)
		 .channel(NioServerSocketChannel.class)
		 .option(ChannelOption.SO_BACKLOG, 1024)
		 //new ChannelHandler extends ChannelInitializer
		 .childHandler(new ChannelInitializer<SocketChannel>() {//new ChannelHandler extends ChannelInitializer
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				//Inserts a ChannelHandlers at the last position of this pipeline.
				ch.pipeline().addLast(new TimeServerHandler());//extends ChannelHandlerAdapter
			}
		});
			//绑定端口，同步等待成功
			ChannelFuture future=b.bind(port).sync();
			//等待服务端监听端口关闭
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//优雅退出，释放线程池资源
			workGroup.shutdownGracefully();
			ioGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args){
		int port = 8080;
		if(args!=null&&args.length>0){
			 port=Integer.valueOf(args[0]);
		}
		new TimeServer().bind(port);
	}
}
