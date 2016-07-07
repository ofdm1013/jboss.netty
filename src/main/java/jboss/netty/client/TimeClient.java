package jboss.netty.client;



import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
	
	public void connect(String host,int port){
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		try {
			Bootstrap b=new Bootstrap();
			b.group(workGroup)
			 .channel(NioSocketChannel.class)
			 .option(ChannelOption.TCP_NODELAY, true)
			 .handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addFirst(new TimeClientHandler());
				}
			});
			//发起异步连接请求
			io.netty.channel.ChannelFuture future=b.connect(host, port).sync();
			//等待客户端连接关闭
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			workGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args){
		int port = 8080;
		if(args!=null&&args.length>0){
			 port=Integer.valueOf(args[0]);
		}
		for(int i=0;i<10;i++){
			new TimeClient().connect("127.0.0.1", port);
		}
		
	}
}
