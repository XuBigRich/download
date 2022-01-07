package cn.piao888.testdownload.slice;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 完美分割一整个文件  ,然后合并
 * 如果是web服务，分割工作由前端完成
 * <p>
 * 使用Files 与Paths进行文件的读取复制
 *
 * @Author： hongzhi.xu
 * @Date: 2021/11/6 10:51 上午
 * @Version 1.0
 */
public class SliceDownload {

    public static void main(String[] args) throws IOException {
        //首先分割文件 得到文件分割的份数
        getSplitFile();
        String file = SplitFileParam.file; //文件的路径
        RandomAccessFile raf  = new RandomAccessFile(new File(file), "r");
        long length = raf.length();//文件的总长度
        long maxSize = SplitFileParam.maxSize;//文件切片后的长度
        long count = length / maxSize; //文件分割的份数
        merge(SplitFileParam.outfile, SplitFileParam.file, 16);
//        remove(SplitFileParam.outfile, SplitFileParam.file, 16);
    }

    /**
     * 文件分割方法。
     * 这里是固定分割几个文件，
     * 但在实际业务中，是需要根据 每个分片大小去确定需要分割成几个文件的。
     */
    public static void getSplitFile() {
        String file = SplitFileParam.file; //文件的路径

        RandomAccessFile raf = null;
        try {
            //获取目标文件 预分配文件所占的空间 在磁盘中创建一个指定大小的文件   r 是只读
            raf = new RandomAccessFile(new File(file), "r");
            long length = raf.length();//文件的总长度 以字节为单位。
//            long maxSize = SplitFileParam.maxSize;//文件切片后的长度
//            long count = length / maxSize; //文件分割的份数
            long count = SplitFileParam.count;
            long maxSize = length / count; //被分割后每个文件的大小
            long offSet = 0L;//初始化偏移量
            for (long i = 0; i < count; i++) { //最后一片单独处理  我这样计算出来的 count  本身  就是 吧 最后一片 除掉的
                long begin = offSet;
                long end = (i + 1) * maxSize;
//                offSet = writeFile(file, begin, end, i);
                offSet = getWrite(file, i, begin, end);
            }
            //文件大小 减去最终偏移量 计算 还剩多少没有分割
            if (length - offSet > 0) {
                //给他割出来
                getWrite(file, count, offSet, length);
            }

        } catch (FileNotFoundException e) {
            System.out.println("没有找到文件");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定文件每一份的边界，写入不同文件中
     *
     * @param file  源文件
     * @param i     源文件的顺序标识
     * @param begin 开始指针的位置
     * @param end   结束指针的位置
     * @return long
     */
    public static long getWrite(String file, long i, long begin, long end) {
        String sourceFileName = file.split("\\.")[0];
        long endPointer = 0L;
        byte[] data = null;
        try {
            //申明文件切割后的文件磁盘
            RandomAccessFile in = new RandomAccessFile(new File(file), "r");
            //定义一个可读，可写的文件并且后缀名为.tmp的二进制文件
            RandomAccessFile out = new RandomAccessFile(new File(sourceFileName + "_" + i + ".tmp"), "rw");
            //申明具体每一文件的字节数组
            byte[] b = new byte[1024];
            int n = 0;
            //从指定位置读取文件字节流
            in.seek(begin);
            //判断文件流读取的边界
            //方法返回当前在此文件中的偏移。 当前文件偏移量不可以大于，最大限制
            long lastPoint = 0;
            while ((lastPoint = in.getFilePointer()) < end && (n = in.read(b)) != -1) {
                //只有一个可能会进来，就是第一次判断时，文件读取指针没有超出。但是执行完in.read超了
                //(这个判断很关键)
                if (in.getFilePointer() > end) {
                    int residue = (int) (end - lastPoint);
                    System.out.println(residue + "要超了");
                    out.write(b, 0, residue);
                } else {
                    out.write(b, 0, n);
                }
                //从指定每一份文件的范围，写入不同的文件
//                baos.write(b, 0, n);
            }
            //关闭输入流
            in.close();
            //关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return end;
    }

    public static void remove(String file, String tempFile, long count) {
        String sourceFileName = tempFile.split("\\.")[0];
        for (int i = 0; i < count; i++) {
            try {
                Files.delete(Paths.get(sourceFileName + "_" + i + ".tmp"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件合并
     *
     * @param file     指定合并文件（输出文件） 的绝对路径
     * @param tempFile 输入的文件（原文件"不是分割文件"） 的绝对路径
     * @param count    文件个数
     */
    public static void merge(String file, String tempFile, long count) {
        String sourceFileName = tempFile.split("\\.")[0];
        try {
            //获取
            Files.createFile(Paths.get(file));
            //开始合并文件，对应切片的二进制文件
            for (int i = 0; i < count; i++) {
                //读取切片文件
                Path path = Paths.get(sourceFileName + "_" + i + ".tmp");
                //将文件输出到SplitFileParam.outfile，挨个读取path临时文件
                Files.write(Paths.get(file), Files.readAllBytes(path), StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
