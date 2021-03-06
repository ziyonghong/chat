package com.qq.client.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.qq.client.tools.ClientConServerThread;
import com.qq.client.tools.ManageClientConServerThread;
import com.qq.common.*;

/*
 * 客户端连接服务器的后台
 */
public class QqClientConServer {

	/*
	 * public QqClientConServer(){ try{ Socket s=new Socket("localhost",8888);
	 * }catch(Exception e){ e.printStackTrace(); }finally{
	 * 
	 * } }
	 */
	public Socket s;
	// 发送第一次请求

	public boolean sendLoginInfoToServer(Object o) {
		boolean b = false;
		try {
			System.out.println("aa");
			s = new Socket("localhost", 8888);
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);

			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			Message ms = (Message) ois.readObject();
		
			//这里就是验证用户登录的地方
			if (ms.getMesType().equals("1")) {
				//就创建一个该qq号和服务器端保持通讯连接的线程
				ClientConServerThread ccst=new ClientConServerThread(s);
				//启动该通讯线程
				ccst.start();
				ManageClientConServerThread.addClientConServerThread
				(((User)o).getUserId(), ccst);
			
				b = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return b;
	}
}
