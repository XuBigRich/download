package cn.piao888.network.exception;

/**
 * 通用业务异常处理
 * @author 赵兴磊
 * @date 2021/8/24 15:00
 */
public class BaseServiceException extends RuntimeException {

    private static final long serialVersionUID = -5754126462432027437L;
    private static final String DELIMITER = "&&&&";

    /**
     *
     * @param message  错误信息
     */
    public BaseServiceException(String message) {
        super(message);
    }

    /**
     *
     * @param message 错误信息
     * @param code 自定义状态码
     */
    public BaseServiceException(String message,int code) {
        super(message+DELIMITER+code);
    }
}
