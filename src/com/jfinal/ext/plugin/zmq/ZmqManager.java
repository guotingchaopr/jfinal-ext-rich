package com.jfinal.ext.plugin.zmq;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class ZmqManager {
	private static final ZContext ctx = new ZContext();
	private String url;
	private int type;
	private ZMQ.Socket socket;

	public ZmqManager(String url, int type) {
		this.url = url;
		this.type = type;
	}

	public boolean init() {
		socket = ctx.createSocket(type);
		return socket.connect(url);
	}

	public ZMQ.Socket getSocket() {
		return socket;
	}

	public void send(byte[] clientId, byte[] msgType, String msgContent) {
		send(clientId, msgType, msgContent.getBytes());
	}

	public void send(byte[] clientId, byte[] msgType, byte[] msgContent) {
		if (clientId == null || msgType == null || msgContent == null) {
			return;
		}
		ZMsg msg = new ZMsg();
		msg.addFirst(clientId);
		msg.addLast(msgType);
		msg.addLast(msgContent);
		msg.send(socket);
	}

	public void destroy() {
		if (ctx != null) {
			ctx.destroy();
		}
	}
}
