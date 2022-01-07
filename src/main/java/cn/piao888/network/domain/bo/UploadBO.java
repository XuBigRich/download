package cn.piao888.network.domain.bo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author： hongzhi.xu
 * @Date: 2022/1/1 9:20 下午
 * @Version 1.0
 */
@Data
public class UploadBO {
    /**
     * 第几个切片
     */
    private Integer chunkNumber;
    /**
     * 理想化切片大小
     */
    private Integer chunkSize;
    /**
     * 当前切片大小
     */
    private Integer currentChunkSize;
    /**
     * 文件
     */
    private MultipartFile file;
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 总切片数
     */
    private Long totalChunks;
    /**
     * 文件大小
     */
    private String totalSize;
    /**
     * 文件md5
     */
    private String md5;

}
