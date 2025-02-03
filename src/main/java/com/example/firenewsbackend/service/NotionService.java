package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.NotionMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotionService {
    @Resource
    private NotionMapper notionMapper;

    private final List<Notion> notions = new ArrayList<>();

    public List<Notion> getAllNotion() {
        return notionMapper.selectList(null);
    }

    public Notion addNotion(Notion notion) {
        return notionMapper.addNotion(notion);
    }

    public Notion updateNotion(Notion notion) {
        return notionMapper.updateNotion(notion);
    }

    public Notion deleteNotion(Integer id) {
        Notion notion = new Notion();
        notion.setId(id);
        notion.setIsDelete(1);
        return notionMapper.updateById(notion) > 0 ? notion : null;
    }

    public List<Notion> getActiveNotifications(String userId) {
        // 获取有效的通知
        return notionMapper.getActiveNotifications(userId);
    }

    public void markAsRead(Integer id) {
        // 标记通知为已读
        notionMapper.markAsRead(id);
    }
}

