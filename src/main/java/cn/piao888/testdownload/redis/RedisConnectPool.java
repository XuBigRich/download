package cn.piao888.testdownload.redis;

import redis.clients.jedis.Jedis;

/**
 * redis线程连接池接口
 *
 * @Author： hongzhi.xu
 * @Date: 2022/1/7 8:49 上午
 * @Version 1.0
 */
public interface RedisConnectPool {

    Jedis getResource() throws Exception;

    void release(Jedis jedis) throws Exception;

}
