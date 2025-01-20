package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.NotionMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NotionService {
    @Resource
    private NotionMapper notionMapper;

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
        return notionMapper.updateNotion(notion);
    }
}
