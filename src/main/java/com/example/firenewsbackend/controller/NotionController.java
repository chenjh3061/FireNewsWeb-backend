package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Notion;
import com.example.firenewsbackend.service.NotionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notion")
public class NotionController {
    @Resource
    private NotionService notionService;

    @GetMapping("/getAllNotion")
    public BaseResponse<List<Notion>> getAllNotion() {
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.getAllNotion());
    }

    @PostMapping("/addNotion")
    public BaseResponse<Notion> addNotion(@RequestBody Notion notion) {
        StpUtil.checkRole("admin");
        Notion addedNotion = notionService.addNotion(notion);
        return ResultUtils.success(addedNotion);
    }

    @PostMapping("/updateNotion")
    public BaseResponse<Notion> updateNotion(@RequestBody Notion notion) {
        StpUtil.checkRole("admin");
        Notion updatedNotion = notionService.updateNotion(notion);
        return ResultUtils.success(updatedNotion);
    }

    @PostMapping("/deleteNotion")
    public BaseResponse<Notion> deleteNotion(@RequestParam Integer id) {
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.deleteNotion(id));
    }

    @PostMapping("/markAsRead")
    public BaseResponse<?> markAsRead(@RequestParam Integer id) {
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.markAsRead(id));
    }
}
