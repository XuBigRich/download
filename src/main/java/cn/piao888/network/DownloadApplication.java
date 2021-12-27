package cn.piao888.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 11:08 上午
 * @Version 1.0
 */
@SpringBootApplication
@EnableConfigurationProperties
public class DownloadApplication {
    public static void main(String[] args) {
        SpringApplication.run(DownloadApplication.class, args);
    }
}
