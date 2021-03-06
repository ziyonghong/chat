package com.qq.client.tools;

import java.io.IOException;
import java.io.ObjectInputStream;
/*
 * 这个客户端和服务器端保持通讯的线程
 */
import java.net.Socket;

import com.qq.client.view.QqChat;
import com.qq.client.view.QqFriendList;
import com.qq.common.Message;
import com.qq.common.MessageType;

public class ClientConServerThread extends Thread {
	private Socket s;
	// 构造函数

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}

	public ClientConServerThread(Socket s) {
		this.s = s;
	}

	public void run() {
		while (true) {
			// 不停的读取从服务器发来的消息
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message m = (Message) ois.readObject();
				// System.out.println("读取到从服务器发来的消息" + m.getSender() + " 给 " +
				// m.getGetter() + "内容" + m.getCon());

				if (m.getMesType().equals(MessageType.message_comm_mes)) {

					// 把服务器获得的消息，显示到该显示的界面
					QqChat qqChat = ManageQqChat.getQqChat(m.getGetter() + " " + m.getSender());
					// 显示
					qqChat.showMessage(m);
				} else if (m.getMesType().equals(MessageType.message_ret_onLineFriend)) {
					System.out.println("客户端接收到" + m.getMesType());
					String con = m.getCon();
					String friends[] = con.split(" ");
					// 返回给发送请教的那个，也就是刚刚的setter
					String getter = m.getGetter();

					System.out.println("getter=" + getter);
					// 修改相应的好友列表
					QqFriendList qqFriendList = ManageQqFriendList.getQqFriendList(getter);

					// 更新在线好友
					if (qqFriendList != null) {
						qqFriendList.updateFriend(m);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
