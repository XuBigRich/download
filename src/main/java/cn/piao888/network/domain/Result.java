package cn.piao888.network.domain;

import cn.piao888.network.enums.ResultEnum;
import cn.piao888.network.exception.BaseServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 功能描述:统一结果，请求响应对象
 *
 * @author zhangxzh
 * @date 2021/8/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Result<T> {
    /**
     * 响应代码
     */
    private int code;

    /**
     * 返回的提示信息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private T data;

    /**
     * 正确返回
     *
     * @param <T> 返回的数据类型
     * @return Result
     */
    public static <T> Result<T> ok() {
        return restResult(ResultEnum.SUCCESS.getCode(), null, ResultEnum.SUCCESS.getMsg());
    }

    /**
     * 带数据的正确返回
     *
     * @param data 返回的数据
     * @param <T>  返回的数据类型
     * @return Result
     */
    public static <T> Result<T> ok(T data) {
        return restResult(ResultEnum.SUCCESS.getCode(), data, ResultEnum.SUCCESS.getMsg());
    }

    /**
     * 带数据以及提示信息的正确返回
     *
     * @param data 返回的数据
     * @param msg  返回的提示信息
     * @param <T>  返回的数据类型
     * @return Result
     */
    public static <T> Result<T> ok(T data, String msg) {
        return restResult(ResultEnum.SUCCESS.getCode(), data, msg);
    }

    /**
     * 错误返回
     *
     * @param <T> 返回的数据类型
     * @return Result
     */
    public static <T> Result<T> failed() {
        return restResult(ResultEnum.FAIL.getCode(), null, ResultEnum.FAIL.getMsg());
    }

    /**
     * 带提示信息的错误返回
     *
     * @param msg 返回的提示信息
     * @param <T> 返回的数据类型
     * @return Result
     */
    public static <T> Result<T> failed(String msg) {
        return restResult(ResultEnum.FAIL.getCode(), null, msg);
    }

    /**
     * 带数据的错误返回
     *
     * @param data 返回的数据
     * @param <T>  返回的数据类型
     * @return Result
     */
    public static <T> Result<T> failed(T data) {
        return restResult(ResultEnum.FAIL.getCode(), data, ResultEnum.FAIL.getMsg());
    }

    /**
     * 带数据以及提示信息的错误返回
     *
     * @param data 返回的数据
     * @param msg  返回的提示信息
     * @param <T>  返回的数据类型
     * @return Result
     */
    public static <T> Result<T> failed(T data, String msg) {
        return restResult(ResultEnum.FAIL.getCode(), data, msg);
    }

    /**
     * 自定义状态码
     *
     * @param code 状态码
     * @param data 返回的数据
     * @param msg  返回的提示信息
     * @param <T>  返回的数据类型
     * @return Result
     */
    public static <T> Result<T> other(int code, T data, String msg) {
        return restResult(code, data, msg);
    }

    private static <T> Result<T> restResult(int code, T data, String msg) {
        if (data == null) {
            data = (T) "";
        }
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回调用成功的数据
     * 只有当code为200的时候才能获取到数据
     *
     * @return 数据
     */
    @JsonIgnore
    public T successData() {
        if (this.code == ResultEnum.SUCCESS.getCode()) {
            return this.data;
        }
        throw new BaseServiceException(this.msg, this.code);
    }
}
