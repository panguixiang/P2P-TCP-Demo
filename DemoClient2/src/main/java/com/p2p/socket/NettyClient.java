package com.p2p.socket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 模拟App客户端TCP请求
 * @author pacy.pan
 *
 */
public class NettyClient {
	final public static String clientId="2222";
	final public static String otherClientId="1111";
	final public static String server_ip="10.5.1.181";
	public void connect(String host, int port) throws Exception {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ClientHandlerInitializer ddd = new ClientHandlerInitializer();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.option(ChannelOption.SO_REUSEADDR, true);
			b.handler(ddd);
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}

	}

	public static void doddd(){
		final NettyClient client = new NettyClient();
		try {
			new Thread(new Runnable() {
				public void run() {
					try {
						client.connect(server_ip, 35000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			System.out.println("-----------Netty服务器无法连接-------------");
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		doddd();
	}
}