package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.model.entity.Notion;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sendNotion")
    @SendTo("/topic/notion")
    public Notion sendNotion(Notion notion) {
        return notion;
    }
}
