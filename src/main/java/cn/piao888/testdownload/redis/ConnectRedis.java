package cn.piao888.testdownload.redis;

import cn.piao888.testdownload.threadUtil.ThreadPools;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 许鸿志
 * @since 2021/10/28
 */
public class ConnectRedis {
    static Jedis jedis;

    static {
        jedis = new Jedis("localhost");
        jedis.auth("Kshanp8848");
    }

    public static Jedis getJedis() {
        // 如果 Redis 服务设置了密码，需要下面这行，没有就不需要
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        return jedis;
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPools=ThreadPools.getThreadPoolExecutor();

        for(;;){
            threadPools.execute(()->{
                Jedis jedis=ConnectRedis.getJedis();
            });
        }
    }

}
