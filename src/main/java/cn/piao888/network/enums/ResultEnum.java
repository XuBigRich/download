package cn.piao888.network.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述:错误枚举，编码和消息
 *
 * @author zhangxzh
 * @date 2021/8/18
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
  /**
   * 处理成功
   */
  SUCCESS(200, "处理成功"),
  /**
   * 处理失败
   */
  FAIL(500, "处理失败"),
  /**
   * 参数错误
   */
  PARAM_ERROR(400, "参数错误"),
  /**
   * 无权限
   */
  NO_POWER_ERROR(403, "无权限"),
  /**
   * token失效
   */
  TOKEN_EXPIRED(498, "token失效"),

  /**
   * refresh token失效
   */
  REFRESH_TOKEN_EXPIRED(499, "refresh token失效");

  private final int code;

  private final String msg;
}
