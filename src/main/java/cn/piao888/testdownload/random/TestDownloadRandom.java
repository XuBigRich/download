package cn.piao888.testdownload.random;

import cn.piao888.testdownload.threadUtil.ThreadPools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 确定偏移量和大小的关系
 * 大小是指文件整体， 偏移量是文件整体的位置
 *
 * @author 许鸿志
 * @since 2021/10/28
 */

/**
 * cpu密集型 推荐线程数     CPU核数+1个线程的线程池
 * io密集型 推荐的线程数    为cpu核数*2
 */
public class TestDownloadRandom {
    static int copySize = 100;
    static int processNum = Runtime.getRuntime().availableProcessors();
    static CountDownLatch countDownLatch;

    public static class Work implements Runnable {
        private RandomAccessFile rafR;
        private RandomAccessFile rafW;
        private long starPoint;

        public Work(File fR, File fW, long startPoint) {
            try {
                this.rafR = new RandomAccessFile(fR, "r");
                this.rafW = new RandomAccessFile(fW, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.starPoint = startPoint;
        }

        @Override
        public void run() {
            try {
                rafR.seek(starPoint);
                rafW.seek(starPoint);
                byte[] bytes = new byte[1024];
                //负责记录从 输入流中每次读取了多少数据
                int len = 0;
                //负责记录累计从输入流中读取了多少数据/ 一共负责写入了多少数据
                long maxSize = 0;
                //保证每个线程不要处理多了 ，只需要再偏移量的基础上 处理 copySize就可以
                //每次最大读取len 长度，首先要确保bytes数组可以正常存放下 这个长度，否则可能会撑爆数组内存
                //所以记录 总读取大小 不要超过 copySize （&& 右边的判断）
                while ((len = rafR.read(bytes)) != -1 && maxSize < copySize) {
                    rafW.write(bytes, 0, len);
                    maxSize = maxSize + len;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    rafR.close();
                    rafW.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

//        File f1 = new File("D:\\迅雷下载\\zypt_202110270934.sql");
        File f1 = new File("D:\\迅雷下载\\zh-cn_windows_10.iso");
        File f2 = new File("C:\\Users\\DELL\\Desktop\\zh-cn_windows_10.iso");
        RandomAccessFile rafR = new RandomAccessFile(f1, "r");
        RandomAccessFile rafW = new RandomAccessFile(f2, "rw");
        //读取源文件大小
        long length = rafR.length();
        //设置目的文件大小
        rafW.setLength(length);
        //总大小 除以 启动线程个数  计算每个线程负责复制的数量大小
        copySize = (int) length / processNum;
        //建立线程等待个数，控制程序退出
        countDownLatch = new CountDownLatch(processNum);
        System.out.println(countDownLatch.getCount());
        //启动线程 去处理复制问题
        for (int i = 0; i <= processNum; i++) {
            ThreadPoolExecutor threadPoolExecutor = ThreadPools.getThreadPoolExecutor();
            threadPoolExecutor.execute(new Work(f1, f2, i * copySize));
        }
        //如果每个线程所最大处理数 处理完后 ，还余下一点
        long residue = length % copySize;
        //主线程去处理余下的一点点
        if (residue != 0) {
            //计算出剩余的需要复制的位置
            long endPont = length - residue;
            rafR.seek(endPont);
            rafW.seek(endPont);
            byte[] bytes = new byte[1024];
            int len = 0;
            //直接读取到无数据可读,前面已经声明好，从文件的seek坐标开始读取，一直读取到最后
            while ((len = rafR.read(bytes)) != -1) {
                //根本不用害怕写重复，因为只是相同数据的覆盖。
                rafW.write(bytes, 0, len);
            }
            rafR.close();
            rafW.close();
        }
        countDownLatch.countDown();
        countDownLatch.await();
        System.out.println("运行");
        System.exit(0);
    }
}
