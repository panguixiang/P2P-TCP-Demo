package com.p2p.client;


import com.p2p.socket.NettyClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 模拟App客户端TCP业务处理
 * @author pacy.pan
 *
 */
public class NettyClientIntHander2 extends ChannelInboundHandlerAdapter {
	
	// 接收server端的消息，并打印出来
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String message = (String)msg;
		System.out.println("----客户端"+NettyClient.clientId+",通过P2P接收对方发送来的消息-----"+message);
		ctx.channel().writeAndFlush("-------------hello,I am --------------"+NettyClient.clientId);
	}

	// 连接成功后，向server发送消息
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().writeAndFlush("-------------hello,I am --------------"+NettyClient.clientId);
	}
}