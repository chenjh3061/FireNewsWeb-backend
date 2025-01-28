package com.example.firenewsbackend.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // 处理收到的消息
//        String payload = message.getPayload();
//        System.out.println("Received message: " + payload);
//
//        // 发送响应消息
//        session.sendMessage(new TextMessage("Echo: " + payload));
//    }
//}
