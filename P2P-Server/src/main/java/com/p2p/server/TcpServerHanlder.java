package com.p2p.server;

import java.net.InetSocketAddress;

import com.p2p.ServerMain;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpServerHanlder extends ChannelInboundHandlerAdapter {

	/*
	 * channelAction
	 * 
	 * channel 通道 action 活跃的
	 * 
	 * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
	 * 
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	/*
	 * channelInactive
	 * 
	 * channel 通道 Inactive 不活跃的
	 * 
	 * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
	 * 
	 */
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
	}

	/*
	 * channelRead
	 * 
	 * channel 通道 Read 读
	 * 
	 * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据 但是这个数据在不进行解码时它是ByteBuf类型的后面例子我们在介绍
	 * 
	 */
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String message = (String)msg;
		System.out.println("---server1-------"+message);
		String[] messageArr = message.split("-");
		if(message.contains("KP")){//心跳 KP-1111
			if(messageArr[1].equals("1111")){
				ServerMain.channel1111=ctx.channel();
			}
			if(messageArr[1].equals("2222")){
				ServerMain.channel2222=ctx.channel();
			}
			ctx.channel().writeAndFlush("KP");//回复客户端，已经收到心跳，并将其channel存入缓存集合
		}else if(message.contains("DD")){//打洞申请 DD-1111-2222 表示1111要和2222建立p2p打洞直连
			/**
			 * 服务端告诉对方客户端进行打洞准备
			 * 这一步没有发送成功 *****
			 */
			Channel otherChannel = null;
			if(messageArr[2].equals("1111")){
				otherChannel=ServerMain.channel1111;
			}
			if(messageArr[2].equals("2222")){
				otherChannel=ServerMain.channel2222;
			}
			if(otherChannel!=null){
				otherChannel.writeAndFlush("CLIENTDD-"+messageArr[2]+"-"+messageArr[1]);
			}else {
				Thread.sleep(2000);
				if(messageArr[2].equals("1111")){
					otherChannel=ServerMain.channel1111;
				}
				if(messageArr[2].equals("2222")){
					otherChannel=ServerMain.channel2222;
				}
				otherChannel.writeAndFlush("CLIENTDD-1111");
			}
		}else if(message.contains("ENDPOINT")){//客户端发送打洞准备已经就绪
			/**ENDPOINT-1111-2222
			 * 得到客户端的公网IP和端口
			 */
			InetSocketAddress insocket = (InetSocketAddress)ctx.channel().remoteAddress();
			String clientIP = insocket.getAddress().getHostAddress();
			int clientPort = insocket.getPort();
			System.out.println("***********clientIP:"+clientIP+" *** clientPort:"+clientPort);
			/**
			 * 发送对方，执行打洞指令
			 */
			System.out.println("***********messageArr[2]==="+messageArr[2]);
			
			if(messageArr[2].contains("1111")){
				Channel other=ServerMain.channel1111;
				System.out.println("***********other==="+other);
				other.writeAndFlush("DOP2P"+"-"+clientIP+"-"+clientPort);
			}
			if(messageArr[2].contains("2222")){
				Channel other=ServerMain.channel2222;
				System.out.println("***********other==="+other);
				other.writeAndFlush("DOP2P"+"-"+clientIP+"-"+clientPort);
			}
			
		//	other.writeAndFlush("DOP2P"+"-"+clientIP+"-"+clientPort);
		}
	}

	/*
	 * channelReadComplete
	 * 
	 * channel 通道 Read 读取 Complete 完成
	 * 
	 * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
	 * 
	 */
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	/*
	 * exceptionCaught
	 * 
	 * exception 异常 Caught 抓住
	 * 
	 * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
	 * 
	 */
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	}

}
