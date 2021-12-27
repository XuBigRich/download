package cn.piao888.network.utils;

import cn.piao888.network.constant.MimeTypeUtils;
import cn.piao888.network.exception.GlobalException;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 验证是否支持此文件类型
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 10:20 下午
 * @Version 1.0
 */
public class FileUploadUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;


    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws GlobalException {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
            throw new GlobalException("文件超过" + DEFAULT_MAX_SIZE / 1024 / 1024);
        }
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new GlobalException("不支持的图片类型");
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new GlobalException("不支持的flash类型");
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new GlobalException("不支持的音频类型");
            } else {
                throw new GlobalException("不支持的文件格式");
            }
        }
    }

    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}
