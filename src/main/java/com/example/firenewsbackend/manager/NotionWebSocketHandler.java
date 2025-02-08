package com.example.firenewsbackend.manager;

import com.example.firenewsbackend.service.NotionService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;

public class NotionWebSocketHandler extends TextWebSocketHandler {
    private final NotionService notionService;

    public NotionWebSocketHandler(NotionService notionService) {
        this.notionService = notionService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        notionService.registerSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // 这里可以处理客户端发送的消息，目前暂时不做处理
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        notionService.unregisterSession(session);
    }
}

