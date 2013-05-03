package com.jfinal.ext.plugin.jedis;

import com.jfinal.plugin.IPlugin;

public class RedisPlugin implements IPlugin {
	private static RedisManager redisManager;

	public RedisPlugin(String host, int port, int dbIndex) {
		RedisPlugin.redisManager = new RedisManager(host, port, dbIndex);
	}

	@Override
	public boolean start() {
		redisManager.init();
		return true;
	}

	@Override
	public boolean stop() {
		redisManager.destroy();
		return true;
	}

}
