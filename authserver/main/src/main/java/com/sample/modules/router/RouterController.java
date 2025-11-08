package com.sample.modules.router;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * TODO 写明类的作用
 *
 * @author Li.HongKun
 * @project authserver
 * @title 登录
 * @package com.example.modules.login.controller
 * @since 2023/9/1 20:16
 */
@Controller
public class RouterController {

    // web平台路由
    @GetMapping("/web")
    public String webHome() {
        return "web/home";
    }

    // 管理平台路由
    @GetMapping("/manager")
    public String managerHome() {
        return "manager/home";
    }
}
