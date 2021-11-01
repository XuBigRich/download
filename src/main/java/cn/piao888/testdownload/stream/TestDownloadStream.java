package cn.piao888.testdownload.stream;

import cn.piao888.testdownload.redis.ConnectRedis;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 断点续传 经过思考 ，只能使用RandomAccessFile
 *
 * @author 许鸿志
 * @since 2021/10/28
 */
public class TestDownloadStream {
    public static Jedis jedis = ConnectRedis.getJedis();
    static byte[] buff = new byte[1024];

    public static void download() {
        jedis.del("readPosition", "readSize", "file", "readPosition", "writePosition");
        int i = 0;
        try (
                FileInputStream input = new FileInputStream("D:\\迅雷下载\\zypt_202110270934.sql");
                FileOutputStream out = new FileOutputStream("C:\\Users\\DELL\\Desktop\\zh-cn_windows_10.iso")) {
            while (i != -1) {
                i = input.read(buff);
                jedis.set("readSize", input.available() + "");
                jedis.set("readPosition", Long.valueOf(jedis.get("readPosition") == null ? "0" : jedis.get("readPosition")) + i + "");
                jedis.set("file", i + "");
                out.write(buff);
                jedis.set("writePosition", Long.valueOf(jedis.get("writePosition") == null ? "0" : jedis.get("writePosition")) + i + "");
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
