package cn.piao888.testdownload.redis;

import cn.piao888.testdownload.threadUtil.ThreadPools;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author： hongzhi.xu
 * @Date: 2022/1/7 8:57 上午
 * @Version 1.0
 */
public class RedisConnectPoolImpl implements RedisConnectPool {
    /**
     * 资源获取计数器(用于描述当前可用的Jedis数量)
     */
    public AtomicInteger atomicInteger;
    public LinkedBlockingQueue<Jedis> jedisPools;
    //最大连接数量
    private int max;
    //超时时间
    private long timeOut;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    private static Jedis createJedis() {
        Jedis jedis = new Jedis("localhost");
        jedis.auth("Kshanp8848");
        return jedis;
    }

    private RedisConnectPoolImpl(Builder builder) throws InterruptedException {
        this.max = builder.max;
        this.timeOut = builder.timeOut;
        jedisPools = new LinkedBlockingQueue(builder.max);
        atomicInteger = new AtomicInteger(0);
        //将队列 扩满
        while (atomicInteger.get() != this.max) {
            jedisPools.put(createJedis());
            atomicInteger.getAndIncrement();
        }
    }

    @Override
    public Jedis getResource() throws Exception {
        Long timeout = System.currentTimeMillis() + timeOut;
        lock.lock();
        //如果可用资源为0，那么就进入循环等待
        while (atomicInteger.get() == 0) {
            condition.await(3, TimeUnit.SECONDS);
            if (timeout < System.currentTimeMillis()) {
                throw new Exception("获取资源超时");
            }
        }
        atomicInteger.decrementAndGet();
        Jedis jedis = jedisPools.take();
        lock.unlock();
        return jedis;
    }

    @Override
    public void release(Jedis jedis) throws Exception {
        Long timeout = System.currentTimeMillis() + timeOut;
        lock.lock();
        //如果可用资源为0，那么就进入循环等待
        while (atomicInteger.get() == this.max) {
            condition.await(3, TimeUnit.SECONDS);
            if (timeout < System.currentTimeMillis()) {
                throw new Exception("获取资源超时");
            }
        }
        atomicInteger.incrementAndGet();
        jedisPools.put(jedis);
        lock.unlock();
    }

    public static class Builder {
        //最大连接数量
        private int max = 5;
        //超时时间
        private long timeOut = 3000;

        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        public Builder setTimeOut(long timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public RedisConnectPoolImpl build() {
            try {
                return new RedisConnectPoolImpl(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void main(String[] args) {
        RedisConnectPool redisConnectPool=new RedisConnectPoolImpl.Builder().build();
        ThreadPoolExecutor threadPools = ThreadPools.getThreadPoolExecutor();
       while (true){
           threadPools.execute(() -> {
               try {
                   Jedis jedis = redisConnectPool.getResource();
                   Long count = jedis.incr("uploadBO.getMd5()");
                   System.out.println(count);
                   //注释掉下面一代码，可以测出获取redis连接超时
                   if(new Random(System.currentTimeMillis()).nextInt()%2==0){
                       //每隔6秒释放一批连接给线程池
                     Thread.sleep(6000);
                   }
                   redisConnectPool.release(jedis);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           });
       }
    }
}
