package com.p2p;

import com.p2p.server.TcpNettyServer;
import io.netty.channel.Channel;

public class ServerMain {
	
	public static Channel channel1111=null;
	public static Channel channel2222=null;
	
	public void start() throws Exception {
		new Thread(new Runnable() {  
		    public void run() {
		    	new TcpNettyServer(35000).Start();
		    }
		}).start();
	}

	public static void main(String[] args) throws Exception {
		ServerMain serverMain = new ServerMain();
		serverMain.start();
	}

}
