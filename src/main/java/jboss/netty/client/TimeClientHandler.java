package jboss.netty.client;



import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
	
	private ByteBuf firstMessage;
	
	public TimeClientHandler() {
		System.out.println(firstMessage);
		byte[] req="QUERY TIME ORDER".getBytes();
		//Creates a new big-endian Java heap buffer with the specified capacity
		firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
		System.out.println(firstMessage);
	}
	
	@Override
	//服务端返回应答消息时，该方法被调用
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf=(ByteBuf)msg;
		System.out.println("buf readInt："+buf.readInt());
		System.out.println("buf readInt："+buf.readInt());
	//	System.out.println("buf readableBytes :" + buf.readableBytes());
		byte[] req=new byte[buf.readableBytes()];
		buf.readBytes(req);//buf中的内容读取到目标数组req，当前buf readIndex +=req.length
		String body=new String(req,"UTF-8");
		System.out.println("Now is "+ body);
		
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		//c-s建立TCP连接后，将请求消息发送至服务端
		ctx.writeAndFlush(firstMessage); 
	}
}
