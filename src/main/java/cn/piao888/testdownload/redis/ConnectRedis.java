package cn.piao888.testdownload.redis;

import redis.clients.jedis.Jedis;

/**
 * @author 许鸿志
 * @since 2021/10/28
 */
public class ConnectRedis {
    static Jedis jedis;

    static {
        jedis = new Jedis("localhost");
        jedis.auth("123456");
    }

    public static Jedis getJedis() {
        // 如果 Redis 服务设置了密码，需要下面这行，没有就不需要
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        return jedis;
    }

}
