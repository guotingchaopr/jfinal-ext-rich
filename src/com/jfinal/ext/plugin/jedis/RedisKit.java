package com.jfinal.ext.plugin.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

public class RedisKit {
	private static final Logger logger = LoggerFactory.getLogger(RedisKit.class);
	private static volatile RedisManager redisManager;

	static void init(RedisManager redisManager) {
		RedisKit.redisManager = redisManager;
	}

	public static Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = redisManager.getJedisPool().getResource();
			jedis.select(redisManager.getDbIndex());
			logger.debug("从pool里获取redis" + redisManager.getDbIndex() + "成功," + jedis);
		} catch (Exception e) {
			logger.error("", e);
		}
		if (jedis == null) {
			try {
				jedis = new Jedis(redisManager.getHost(), redisManager.getPort(), 2000);
				jedis.select(redisManager.getDbIndex());
				logger.debug("创建redis" + redisManager.getDbIndex() + "成功," + jedis);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return jedis;
	}

	public static void returnResource(Jedis jedis) {
		if (redisManager == null || redisManager.getJedisPool() == null || jedis == null) {
			return;
		}
		redisManager.getJedisPool().returnResource(jedis);
		logger.debug("释放redis回pool里," + jedis);
	}
}
