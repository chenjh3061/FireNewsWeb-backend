package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.NotionMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotionService {
    @Resource
    private NotionMapper notionMapper;

    private final List<Notion> notions = new ArrayList<>();
    //private final AtomicInteger idGenerator = new AtomicInteger(1);

    public List<Notion> getAllNotion() {
        return notionMapper.selectList(null);
    }

    public Notion addNotion(Notion notion) {
//        notion.setId(idGenerator.getAndIncrement());
//        notions.add(notion);
        return notionMapper.addNotion(notion);
    }

    public Notion updateNotion(Notion notion) {
       // notions.replaceAll(n -> n.getId().equals(notion.getId()) ? notion : n);
        return notionMapper.updateNotion(notion);
    }

    public Notion deleteNotion(Integer id) {
        Notion notion = new Notion();
        notion.setId(id);
        notion.setIsDelete(1);
        return notionMapper.updateNotion(notion);
    }
}
