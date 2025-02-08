package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Notion;
import com.example.firenewsbackend.service.NotionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notion")
public class NotionController {
    private final NotionService notionService;

    public NotionController(NotionService notionService) {
        this.notionService = notionService;
    }

    @GetMapping("/getAllNotion")
    public BaseResponse<List<Notion>> getAllNotion() {
        StpUtil.checkRole("admin");
        return ResultUtils.success(notionService.getAllNotion());
    }

    // 获取所有用户的当前生效公告
    @GetMapping("/active")
    public List<Notion> getActiveNotifications() {
        return notionService.getAllActiveNotifications();
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
        return ResultUtils.success(notionService.updateNotion(notion));
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
