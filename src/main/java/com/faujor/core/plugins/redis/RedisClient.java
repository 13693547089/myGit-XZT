package com.faujor.core.plugins.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.faujor.utils.JsonUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by martian on 2017/10/18.
 */
@Component
public class RedisClient {
	@Autowired
	private JedisPool jedisPool;

	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			setRedis(jedis, value, key);
		} finally {
			jedis.close();
		}
	}

	public void setRedis(Jedis jedis, String key, String value) {
		String str = jedis.get(key);
		List<String> list = new ArrayList<String>();
		if (StringUtils.isEmpty(str)) {
			list.add(value);
			String json = JsonUtils.beanToJson(list);
			jedis.set(key, json);
		} else {
			list = JsonUtils.jsonToList(str, String.class);
			int i = list.indexOf(value);
			if (i == -1) {
				list.add(value);
				String json = JsonUtils.beanToJson(list);
				jedis.set(key, json);
			}
		}
	}

	/**
	 * 设置值,包括时效
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void set(String key, String value, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.setex(key, seconds, value);
		} finally {
			// 返还到连接池
			jedis.close();
		}
	}
	
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.get(key);
		} finally {
			// 返还到连接池
			jedis.close();
		}
	}

	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.get(key);
			if (!StringUtils.isEmpty(value))
				delRedis(jedis, value, key);
			jedis.del(key);
		} finally {
			// 返还到连接池
			jedis.close();
		}
	}

	public void delRedis(Jedis jedis, String key, String value) {
		String str = jedis.get(key);
		List<String> list = JsonUtils.jsonToList(str, String.class);
		int i = list.indexOf(value);
		if (i >= 0) {
			list.remove(i);
		}
		if (list.size() > 0) {
			String json = JsonUtils.beanToJson(list);
			jedis.set(key, json);
		} else {
			jedis.del(key);
		}
	}

	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} finally {
			// 返还到连接池
			jedis.close();
		}
	}
	
	/**
	 * 删除key
	 * @param key
	 */
	public void delKey(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} finally {
			// 返还到连接池
			jedis.close();
		}
	}
}