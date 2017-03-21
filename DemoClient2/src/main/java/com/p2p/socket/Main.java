package com.p2p.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.p2p.clientserver.TcpNettyServer;

/**
 * 
 * @author huzhigang
 */
public class Main extends Thread{

	private String clientId;
	private String otherClientid;
	
	public Main(String clientid,String otherClientid){
		this.clientId=clientid;
		this.otherClientid=otherClientid;
	}
	public void run(){
		Socket socket = null;
		ServerSocket serverSocket;
		PrintWriter os = null;
		try {
			/*
			 * 随机分配一个本地端口
			 */
			serverSocket = new ServerSocket(0);
			int localPort =serverSocket.getLocalPort();// 得到那个随机端口
			serverSocket.close();
			/**
			 * 先发送打洞，和固定端口给server，然后立即断开此端口
			 */
			socket = new Socket(NettyClient.server_ip, 35000, InetAddress.getLocalHost(), localPort);
			socket.setReuseAddress(true);
			os = new PrintWriter(socket.getOutputStream());
			os.println("ENDPOINT-"+this.clientId+"-"+this.otherClientid);
			os.flush();
			os.close();
			socket.close();

			Thread.sleep(600);
			/**
			 * 启动同一本地端口做serversocket监听
			 */
			new TcpNettyServer(localPort).start();
		/*	serverSocket = new ServerSocket(localPort);
			socket = serverSocket.accept();
			PrintWriter wtr = new PrintWriter(socket.getOutputStream());
			BufferedReader rdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = rdr.readLine();
			System.out.println("从客户端来的信息：" + line);
			wtr.flush();*/
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			if(os!=null){
				os.close();
			}
			try {
				if(socket!=null){
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}