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

    //    public synchronized static Jedis getJedis() {
    public static Jedis getJedis() {
        // 如果 Redis 服务设置了密码，需要下面这行，没有就不需要
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        return jedis;
    }

    /**
     * 多线程同时 操作同一个 jedis 会报错
     * <p>
     * 对于这个问题 ，我们可以规定 一个线程使用一个jedis，这样就可以避免报错，所以我们需要使用一个线程池
     * <p>
     * 或者我们以但线程的形式调用 getJedis 方法，添加synchronize 关键字
     *
     * @param args
     */
    public static void main(String[] args) {
        ThreadPoolExecutor threadPools = ThreadPools.getThreadPoolExecutor();

        for (; ; ) {
            threadPools.execute(() -> {
                Jedis jedis = ConnectRedis.getJedis();
            });
        }
    }

}
