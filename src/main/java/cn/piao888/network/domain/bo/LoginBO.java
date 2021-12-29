package cn.piao888.network.domain.bo;

import lombok.Data;

/**
 * @author 许鸿志
 * @since 2021/12/29
 */
@Data
public class LoginBO {
    private String username;
    private String password;
    private String role;
}
