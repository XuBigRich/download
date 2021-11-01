package cn.piao888.testdownload.threadUtil;

import java.lang.reflect.Executable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 许鸿志
 * @since 2021/10/28
 */
public class ThreadPools {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 16, 1, TimeUnit.HOURS, new ArrayBlockingQueue(100000));

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

}
