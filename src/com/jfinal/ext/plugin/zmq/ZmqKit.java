package com.jfinal.ext.plugin.zmq;

public class ZmqKit {
	private static volatile ZmqManager zmqManager;

	static void init(ZmqManager zmqManager) {
		ZmqKit.zmqManager = zmqManager;
	}

	public static ZmqManager getZmqManager() {
		return zmqManager;
	}

	public static void send(byte[] clientId, byte[] msgType, String msgContent) {
		zmqManager.send(clientId, msgType, msgContent.getBytes());
	}

	public static void send(byte[] clientId, byte[] msgType, byte[] msgContent) {
		zmqManager.send(clientId, msgType, msgContent);
	}
}
