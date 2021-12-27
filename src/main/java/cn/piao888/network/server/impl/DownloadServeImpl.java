package cn.piao888.network.server.impl;

import cn.piao888.network.config.DownloadProperties;
import cn.piao888.network.exception.GlobalException;
import cn.piao888.network.server.DownloadServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 1:31 下午
 * @Version 1.0
 */
@Service
public class DownloadServeImpl implements DownloadServe {
    @Autowired
    private DownloadProperties downloadProperties;

    @Override
    public void downLoad(String fileName) throws Exception {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/octet-stream");
        OutputStream outputStream = response.getOutputStream();
        WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);
        //******不要使用printWriter*******
        // 会出现 MalformedInputException: Input length = 1 异常
//        出现下载失败的原因：
//        PrintWriter流处理字节数据，会导致数据丢失，因此在编写下载文件功能时，要使用OutputStream流。
//        因为OutputStream流是字节流，可以处理任意类型的数据；
//        而PrintWriter流是字符流，只能处理字符数据，如果用字符流处理字节数据，会导致数据丢失。
        File file = new File(downloadProperties.getProfile() + fileName);
        if (!file.exists()) {
            throw new GlobalException("当前文件不存在");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        //将文件输入流转为文件字节通道,通道是双向的
        FileChannel fileChannel = fileInputStream.getChannel();
//        //将字节通道 转为 字符通道
//        Reader reader = Channels.newReader(fileChannel, "UTF-8");
        fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
        fileChannel.close();
        fileInputStream.close();
    }
}
