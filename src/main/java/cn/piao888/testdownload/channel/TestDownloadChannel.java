package cn.piao888.testdownload.channel;

import cn.piao888.testdownload.redis.ConnectRedis;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestDownloadChannel {
    public static Jedis jedis = ConnectRedis.getJedis();
    static ByteBuffer buff = ByteBuffer.allocate(1024);

    public static void download() {
        jedis.del("readPosition", "readSize", "file", "readPosition", "writePosition");
        int i = 0;
        try (
                FileChannel input = new FileInputStream("D:\\迅雷下载\\zypt_202110270934.sql").getChannel();
                FileChannel out = new FileOutputStream("C:\\Users\\DELL\\Desktop\\zh-cn_windows_10.iso").getChannel()) {
            while (i != -1) {
                buff.clear();
                i = input.read(buff);
                jedis.set("readSize", input.size() + "");
                jedis.set("readPosition", input.position() + "");
                jedis.set("file", i + "");
                buff.flip();
                out.write(buff);
                jedis.set("writePosition", out.position() + "");
                jedis.set("writeSize", input.size() + "");
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        download();
    }

}
