package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Notion;
import com.example.firenewsbackend.service.NotionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notion")
public class NotionController {
    @Resource
    private NotionService notionService;

    @GetMapping("/getAllNotion")
    public BaseResponse<List<Notion>> getAllNotion(){
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.getAllNotion());
    }

    @PostMapping("/addNotion")
    public BaseResponse<Notion> addNotion(Notion notion){
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.addNotion(notion));
    }

    @PostMapping("/updateNotion")
    public BaseResponse<Notion> updateNotion(Notion notion){
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.updateNotion(notion));
    }

    @PostMapping("/deleteNotion")
    public BaseResponse<Notion> deleteNotion(Integer id){
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.deleteNotion(id));
    }

}
