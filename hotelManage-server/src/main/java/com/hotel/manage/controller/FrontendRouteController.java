package com.hotel.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 前端路由控制器
 * 用于支持 Vue Router 的 HTML5 History 模式
 */
@Controller
public class FrontendRouteController {

    /**
     * 处理根路径，返回首页
     */
    @GetMapping("/")
    public String handleRoot() {
        return "forward:/index.html";
    }

    /**
     * 处理所有前端路由，返回 index.html
     * 这样可以让Vue Router处理所有的前端路由
     */
    @GetMapping({
        "/home",
        "/reservation",
        "/room",
        "/room-type", 
        "/check-in",
        "/check-out",
        "/guest",
        "/member",
        "/role",
        "/system-user"
    })
    public String handleFrontendRoutes() {
        return "forward:/index.html";
    }
}