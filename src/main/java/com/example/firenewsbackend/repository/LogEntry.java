package com.example.firenewsbackend.repository;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogEntry {
    private String userId;
    private String action;
    private String targetType;  // 比如：文章、用户信息等
    private String targetId;
    private String description;
    private LocalDateTime timestamp;

    // Getters and Setters
}

