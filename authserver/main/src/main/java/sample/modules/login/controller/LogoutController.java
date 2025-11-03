package sample.modules.login.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TODO 写明类的作用
 *
 * @author Li.HongKun
 * @project authserver
 * @title 退出应用
 * @package com.example.modules.login.controller
 * @since 2023/9/1 20:16
 */
@Controller
public class LogoutController {

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @RequestMapping("/my/logout")
    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        // .. perform logout
        this.logoutHandler.logout(request, response, authentication);
        return "redirect:/login";
    }
}
