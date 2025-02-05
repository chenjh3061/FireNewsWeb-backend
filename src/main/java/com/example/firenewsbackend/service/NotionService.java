package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.NotionMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotionService {
    private final NotionMapper notionMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // 构造函数注入
    public NotionService(NotionMapper notionMapper, SimpMessagingTemplate messagingTemplate) {
        this.notionMapper = notionMapper;
        this.messagingTemplate = messagingTemplate;
    }

    public List<Notion> getAllNotion() {
        return notionMapper.selectList(null);
    }

    public Notion addNotion(Notion notion) {
        Notion addedNotion = notionMapper.addNotion(notion);
        // 发送新通知给所有订阅用户
        sendNotification(addedNotion);
        return addedNotion;
    }

    public Notion updateNotion(Notion notion) {
        Notion updatedNotion = notionMapper.updateNotion(notion);
        // 发送更新后的通知给所有订阅用户
        sendNotification(updatedNotion);
        return updatedNotion;
    }

    public Notion deleteNotion(Integer id) {
        Notion notion = new Notion();
        notion.setId(id);
        notion.setIsDelete(1);
        if (notionMapper.updateById(notion) > 0) {
            // 发送删除通知给所有订阅用户
            sendNotification(notion);
            return notion;
        }
        return null;
    }

    public List<Notion> getActiveNotifications(String userId) {
        return notionMapper.getActiveNotifications(userId);
    }

    public Notion markAsRead(Integer id) {
        return notionMapper.markAsRead(id);
    }

    private void sendNotification(Notion notion) {
        messagingTemplate.convertAndSend("/topic/notifications", notion);
    }
}

