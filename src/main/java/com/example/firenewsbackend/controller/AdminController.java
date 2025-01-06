package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/getTodayViewCount")
    public Long getTodayViewCount(){
        StpUtil.checkRole("admin");
        return 12345L;
    }

    @GetMapping("/getUserNum")
    public Long getUserNum(){
        StpUtil.checkRole("admin");
        return adminService.getUserNum();
    }

    @GetMapping("/getArticleNum")
    public Long getArticleNum(){
        StpUtil.checkRole("admin");
        return adminService.getArticleNum();
    }


}
