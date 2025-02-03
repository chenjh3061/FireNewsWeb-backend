package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Notion;
import com.example.firenewsbackend.service.NotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.management.Notification;
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

    @GetMapping("/sse/notifications")
    public SseEmitter getNotifications(@RequestParam String userId) {
        SseEmitter emitter = new SseEmitter();
        try {
            // 获取有效的通知
            List<Notion> notifications = notionService.getActiveNotifications(userId);

            for (Notion notification : notifications) {
                // 推送每个通知
                emitter.send(notification, MediaType.APPLICATION_JSON);
            }

            // 完成推送
            emitter.complete();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
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
        notionService.markAsRead(id);
        return ResultUtils.success("已读");
    }
}
