package cn.piao888.network.controller;

import cn.piao888.network.domain.Result;
import cn.piao888.network.domain.bo.LoginBO;
import cn.piao888.network.domain.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 许鸿志
 * @since 2021/12/29
 */
@RestController
@RequestMapping("user")
public class LoginController {
    @PostMapping("login")
    public Result login(LoginBO loginBO) {
        if(loginBO.getUsername().equals("admin")&&loginBO.getPassword().equals("admin123")){
            return new Result(200,"",new LoginVO());
        }
        return new Result(400,"",null);
    }

}
