package com.example.firenewsbackend.service;

import com.example.firenewsbackend.model.entity.Notion;

import javax.annotation.Resource;
import java.util.List;

public class NotionService {
    @Resource
    private NotionService notionService;

    public List<Notion> getAllNotion() {
        return notionService.getAllNotion();
    }

    public Notion addNotion(Notion notion) {
        return notionService.addNotion(notion);
    }

    public Notion updateNotion(Notion notion) {
        return notionService.updateNotion(notion);
    }

    public Notion deleteNotion(Integer id) {
        return notionService.deleteNotion(id);
    }
}
