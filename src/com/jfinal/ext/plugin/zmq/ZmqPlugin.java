package com.jfinal.ext.plugin.zmq;

import com.jfinal.plugin.IPlugin;

public class ZmqPlugin implements IPlugin {
	private static ZmqManager zmqManager;

	public ZmqPlugin(String url, int type) {
		ZmqPlugin.zmqManager = new ZmqManager(url, type);
	}

	@Override
	public boolean start() {
		if (zmqManager.init()) {
			ZmqKit.init(zmqManager);
			return true;
		}
		return false;
	}

	@Override
	public boolean stop() {
		zmqManager.destroy();
		return true;
	}

}
