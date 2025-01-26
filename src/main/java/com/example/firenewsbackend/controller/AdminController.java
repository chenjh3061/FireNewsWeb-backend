package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Log;
import com.example.firenewsbackend.model.vo.DashboardVO;
import com.example.firenewsbackend.service.AdminService;
import com.example.firenewsbackend.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private LogService  logService;

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

    @GetMapping("/getRecentLog")
    public BaseResponse<List<Log>> getRecentLog(){
        StpUtil.checkRole("admin");
        int limit = 10;
        return ResultUtils.success(logService.getRecentLog(limit));
    }


}
