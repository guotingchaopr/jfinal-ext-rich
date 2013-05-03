package com.jfinal.ext.plugin.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {
	private static JedisPool pool;
	private String host;
	private int port;
	private int dbIndex;

	public RedisManager(String host, int port, int dbIndex) {
		this.host = host;
		this.port = port;
		this.dbIndex = dbIndex;
	}

	public void init() {
		JedisPoolConfig config = new JedisPoolConfig();

		config.setMaxActive(20);
		config.setMaxIdle(20);
		config.setMaxWait(1000);
		config.setTestOnBorrow(true);

		pool = new JedisPool(config, host, port, 2000);
	}

	public JedisPool getJedisPool() {
		return pool;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public void destroy() {
		pool.destroy();
	}
}
