package cn.piao888.network.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 1:29 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties("download")
public class DownloadProperties {
    private String profile;
}

