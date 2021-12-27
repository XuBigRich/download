package cn.piao888.network.handler;

import cn.piao888.network.domain.Result;
import cn.piao888.network.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 功能描述:
 *
 * @author zhangxzh
 * @date 2021-09-14
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = GlobalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleBaseException(Exception e) {
        log.error("系统异常:{}", e);
        return Result.failed(e.getMessage());
    }
}
