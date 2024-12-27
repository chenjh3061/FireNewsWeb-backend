package com.example.firenewsbackend.ws;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class YourWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 这里处理收到的消息
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        // 回复消息
        session.sendMessage(new TextMessage("Hello, " + payload));
    }
}
