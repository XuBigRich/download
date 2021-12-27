package cn.piao888.network.controller;

import cn.piao888.network.constant.MimeTypeUtils;
import cn.piao888.network.server.DownloadServe;
import cn.piao888.network.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 11:22 上午
 * @Version 1.0
 */
@RestController
public class Controller {
    @Autowired
    private DownloadServe downloadServe;

    @GetMapping("down")
    public void down(String fileName) throws Exception {
        downloadServe.downLoad(fileName);
    }

    @PostMapping("upload")
    public void upload(MultipartFile multipartFile) {
        //判断文件类型是否是系统支持
        FileUploadUtils.assertAllowed(multipartFile, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        //
    }
}
