package com.p2p.encoder;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 自定义netty 反序列化使用fastJson高效序列化框架
 * 目前没用上
 * @author Pacy.pan
 *
 */
public class FastJsonMsgDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int by= in.readableBytes();
		if(by==0){
			in.resetReaderIndex();
			return;
		}
        byte[] body = new byte[by];//传输正常
        in.readBytes(body);
		JSONObject obj = (JSONObject) JSON.parse(body);
		out.add(obj);
	}

}