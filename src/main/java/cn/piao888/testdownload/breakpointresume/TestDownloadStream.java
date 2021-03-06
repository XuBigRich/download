package cn.piao888.testdownload.breakpointresume;

import cn.piao888.testdownload.redis.ConnectRedis;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 断点续传 经过思考 ，只能使用RandomAccessFile  (仅限后端，整体文件复制时)
 *
 * 前端的分片 断点续传 是根据分片 跳过解决的。
 *
 * @author 许鸿志
 * @since 2021/10/28
 */
public class TestDownloadStream {
    public static Jedis jedis = ConnectRedis.getJedis();
    static byte[] buff = new byte[1024];

    public static void download() {
//
        int i = 0;
        try (
                RandomAccessFile input = new RandomAccessFile("D:\\迅雷下载\\zypt_202110270934.sql", "r");
//                RandomAccessFile input = new RandomAccessFile("D:\\迅雷下载\\zh-cn_windows_10.iso", "R");
                RandomAccessFile out = new RandomAccessFile("C:\\Users\\DELL\\Desktop\\zypt.sql", "rw")) {
            jedis.set("size", input.length() + "");
            out.setLength(input.length());
            while (true) {
                //确定指针读取到哪里了,如果是第一次读，肯定 从redis中获取到的是一个空的
                //这个地方控制开始写入的起始位置
                input.seek(Long.valueOf(jedis.get("position") == null ? "0" : jedis.get("position")));
                out.seek(Long.valueOf(jedis.get("position") == null ? "0" : jedis.get("position")));
                //读取字节到buff中,i可以真实的描述字节读取了多少
                i = input.read(buff);
                if (i == -1) break;
                //一定要填i 写入长度，否则有可能回 将 bytes多余的字节信息 写到文件里面
                out.write(buff, 0, i);
                //当读取完与写入完毕后，偏移指针
                jedis.set("position", jedis.get("position") == null ? i + "" : Long.valueOf(jedis.get("position")) + i + "");
                break;
            }
            if (i == -1) {
                jedis.del("position", "size");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        download();
    }
}
