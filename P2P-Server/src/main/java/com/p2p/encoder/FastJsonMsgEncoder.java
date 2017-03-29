package com.p2p.encoder;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义netty 序列化使用fastJson高效序列化框架
 * 目前没用上
 * @author Pacy.pan
 *
 */
public class FastJsonMsgEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		byte[] body = JSONObject.toJSONBytes(msg);
		out.writeInt(body.length);  //先将消息长度写入，也就是消息头
        out.writeBytes(body);  //消息体中包含我们要发送的数据
	}
}