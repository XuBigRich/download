package cn.piao888.network.constant;

/**
 * @Author： hongzhi.xu
 * @Date: 2021/12/22 10:09 下午
 * @Version 1.0
 */
public interface MimeTypeUtils {



    String IMAGE_PNG = "image/png";

    String IMAGE_JPG = "image/jpg";

    String IMAGE_JPEG = "image/jpeg";

    String IMAGE_BMP = "image/bmp";

    String IMAGE_GIF = "image/gif";

    String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png" };

    String[] FLASH_EXTENSION = {"swf", "flv" };

    String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb" };

    String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // pdf
            "pdf" };

    static String getExtension(String prefix) {
        switch (prefix) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
