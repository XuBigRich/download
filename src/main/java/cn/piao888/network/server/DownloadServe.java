package cn.piao888.network.server;

import cn.piao888.network.domain.bo.UploadBO;

import java.io.IOException;

/**
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 1:31 下午
 * @Version 1.0
 */
public interface DownloadServe {
    void downLoad(String fileName) throws Exception;

    void upload(UploadBO uploadBO) throws Exception;
}
