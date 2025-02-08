package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.NotionMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotionService {
    private final NotionMapper notionMapper;
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public NotionService(NotionMapper notionMapper) {
        this.notionMapper = notionMapper;
    }

    public List<Notion> getAllNotion() {
        return notionMapper.selectList(null);
    }

    // 新增方法：获取所有用户的当前生效公告
    public List<Notion> getAllActiveNotifications() {
        return notionMapper.getAllActiveNotifications();
    }


    public Notion addNotion(Notion notion) {
        notionMapper.insert(notion);
        return notion;
    }

    public Notion updateNotion(Notion notion) {
        notionMapper.updateNotion(notion);
        return notion;
    }

    public Notion deleteNotion(Integer id) {
        Notion notion = new Notion();
        notion.setId(id);
        notion.setIsDelete(1);
        if (notionMapper.updateById(notion) > 0) {
            sendNotification(notion);
            return notion;
        }
        return null;
    }

    public Notion markAsRead(Integer id) {
        return notionMapper.markAsRead(id);
    }

    public void registerSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void unregisterSession(WebSocketSession session) {
        sessions.remove(session);
    }

    private void sendNotification(Notion notion) {
        try {
            String json = new com.google.gson.Gson().toJson(notion);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
