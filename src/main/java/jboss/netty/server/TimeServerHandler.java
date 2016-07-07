package jboss.netty.server;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 数字0x12345678在两种不同字节序CPU中的存储顺序如下所示：

			Big Endian(高位字节排放在内存的低地址端，低位字节排放在内存的高地址端。)
			
			   低地址                                            高地址
			   ----------------------------------------->
			   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			   |     12     |      34    |     56      |     78    |
			   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			
			Little Endian(低位字节排放在内存的低地址端，高位字节排放在内存的高地址端。)
			
			   低地址                                            高地址
			   ----------------------------------------->
			   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			   |     78     |      56    |     34      |     12    |
			   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 */
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			// TODO Auto-generated method stub
			ByteBuf buf=(ByteBuf)msg;	
			System.out.println("before "+buf.readerIndex());
			
			System.out.println("buf readInt :" +buf.readInt());
			System.out.println("after: "+buf.readerIndex());
			byte[] req=new byte[buf.readableBytes()];//writerIndex - readerIndex;
			//缓冲区的字节数组复制到新建的字节数组
			buf.readBytes(req);//Transfers this buffer's data to the specified destination
			String body=new String(req,"UTF-8");
			System.out.println("RECEIVE ORDER"+body);
			String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
			ByteBuf resp=Unpooled.copiedBuffer(currentTime.getBytes());
			ctx.write(resp);//异步发送消息至客户端
		}
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			//发送队列中的消息写入到socketchannel中发送给对方
			ctx.flush();
		}
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			// TODO Auto-generated method stub
			ctx.close();
		}
}
