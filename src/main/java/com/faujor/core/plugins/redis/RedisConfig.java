package com.faujor.core.plugins.redis;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.beans.factory.annotation.Value;  
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import com.faujor.utils.StringUtil;

import redis.clients.jedis.JedisPool;  
import redis.clients.jedis.JedisPoolConfig;  
  

/**
 * 开启redis缓存
 * 1.@EnableCaching
 * 2.实体需要现实序列化
 * 3.方法上面需要设置@Cacheable
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {  
    Logger logger = LoggerFactory.getLogger(RedisConfig.class);  
  
    /**  
     * SpringSession  需要注意的就是redis需要2.8以上版本，然后开启事件通知，在redis配置文件里面加上  
     * notify-keyspace-events Ex  
     * Keyspace notifications功能默认是关闭的（默认地，Keyspace 时间通知功能是禁用的，因为它或多或少会使用一些CPU的资源）。  
     * 或是使用如下命令：  
     * redis-cli config set notify-keyspace-events Egx  
     * 如果你的Redis不是你自己维护的，比如你是使用阿里云的Redis数据库，你不能够更改它的配置，那么可以使用如下方法：在applicationContext.xml中配置  
     * <util:constant static-field="org.springframework.session.data.redis.config.ConfigureRedisAction.NO_OP"/>  
     * @return  
     */  
  
    @Value("${spring.redis.host}")  
    private String host;  
  
    @Value("${spring.redis.port}")  
    private int port;  
  
    @Value("${spring.redis.timeout}")  
    private int timeout;  
  
    @Value("${spring.redis.pool.max-idle}")  
    private int maxIdle;  
  
    @Value("${spring.redis.pool.max-wait}")  
    private long maxWaitMillis;  
  
    @Value("${spring.redis.password}")  
    private String password;  
  
    //缓存管理器
    @Bean 
    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        //设置缓存过期时间 
        //cacheManager.setDefaultExpiration(10000);
        return cacheManager;
    }
    
    @Bean  
    public JedisPool redisPoolFactory() {  
        logger.info("JedisPool注入成功！！");  
        logger.info("redis地址：" + host + ":" + port);  
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();  
        jedisPoolConfig.setMaxIdle(maxIdle);  
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);  
        JedisPool jedisPool =null;
        if(StringUtil.isNullOrEmpty(password)){
        	jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);//无密码情况
        }else{
        	jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        } 
        return jedisPool;  
    }  
}  