package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.vo.DashboardVO;
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

    @GetMapping("/getDashboardData")
    public BaseResponse<DashboardVO> DashboardData(){
        StpUtil.checkRole("admin");
        DashboardVO dashboardVO = new DashboardVO();
        dashboardVO.setUserNum(adminService.getUserNum());
        dashboardVO.setArticleNum(adminService.getArticleNum());
        dashboardVO.setTodayViewCount(12345L);
        dashboardVO.setRunningDays(102L);
        return ResultUtils.success(dashboardVO);
    }


}
