package com.p2p.socket;


import com.p2p.client.NettyClient2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 模拟App客户端TCP业务处理
 * @author pacy.pan
 *
 */
public class NettyClientIntHander extends ChannelInboundHandlerAdapter {
	
	private static volatile Integer num=0;
	// 接收server端的消息，并打印出来
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String message=(String)msg;
		System.out.println("-----from server message===="+message);
		final String[] otherClientArr = message.split("-");
		
		if(message.contains("KP")){//服务端回复已经收到心跳 
			/**
			 * 向服务端发送一个打洞申请
			 * 并不是所有客户端都需要这么做，只有发起打洞的 那一方才发送此
			 */
			Thread.sleep(1000);
			ctx.channel().writeAndFlush("DD-"+NettyClient.clientId+"-"+NettyClient.otherClientId);
			new Main(NettyClient.clientId,NettyClient.otherClientId).start();
			
		} else if(message.contains("CLIENTDD")){//来自服务端 转发的对方客户端打洞申请
			new Main(otherClientArr[1],otherClientArr[2]).start();
			//客户端启动监听端口，并用此端口发送socket给服务端，让服务端获得此客户端打洞用的NAT分配的对外端口和公网ip
		} else if(message.contains("DOP2P")){//服务端 发送对方NAT的公网IP和端口给客户端
			/**
			 * 例子：DOP2P-123.234.23.43-234232
			 * 客户端使用对方的公网IP和端口进行打洞操作，即再做一个netty链接
			 */
			
			final NettyClient2 client2=new NettyClient2();
			new Thread(new Runnable() {
				
				public void run() {
					while(num==0 || num<3){
						try {
							Thread.sleep(1500);
							client2.connect(otherClientArr[1],Integer.parseInt(otherClientArr[2]));
							num=6;
						} catch (NumberFormatException e) {
							num=num+1;
							e.printStackTrace();
						} catch (Exception e) {
							num=num+1;
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

	// 连接成功后，向server发送消息
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().writeAndFlush("KP-"+NettyClient.clientId);
	}
	
}