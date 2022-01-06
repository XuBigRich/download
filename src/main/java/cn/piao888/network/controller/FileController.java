package cn.piao888.network.controller;

import cn.piao888.network.constant.MimeTypeUtils;
import cn.piao888.network.domain.bo.UploadBO;
import cn.piao888.network.server.DownloadServe;
import cn.piao888.network.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;


/**
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 11:22 上午
 * @Version 1.0
 */
@Controller
public class FileController {
    @Autowired
    private DownloadServe downloadServe;

    @GetMapping("down")
    public void down(String fileName) throws Exception {
        downloadServe.downLoad(fileName);
    }

    @PostMapping("upload")
    public void upload(UploadBO uploadBO) throws ServletException, IOException {
        //判断文件类型是否是系统支持
//        FileUploadUtils.assertAllowed(uploadBO.getFile(), MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        //
//        System.out.println(uploadBO.getFile()uploadBO.getOriginalFilename());
        downloadServe.upload(uploadBO);

    }
}
