package com.p2p.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpServerHandlerInitializer extends ChannelInitializer<NioSocketChannel> {

	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		/**
		 * 实际项目中药将此序列化方式改成FastJsonMsgEncoder和FastJsonMsgDecoder
		 */
		pipeline.addLast("encoder",new StringEncoder());
		pipeline.addLast("decoder",new StringDecoder());
		pipeline.addLast("handler",new TcpServerHanlder());
	}

}
