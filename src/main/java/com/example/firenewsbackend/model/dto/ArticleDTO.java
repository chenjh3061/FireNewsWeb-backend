package com.example.firenewsbackend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleDTO {
    private Long articleId;
    private String articleTitle;
    private String articleDesc;
    private String articleAvatar;
    private Integer articleCategory;
    private String articleContent;
    private Long viewCount;

    private Integer reviewStatus;
    private String reviewMessage;
    private LocalDateTime createTime;
    private LocalDateTime  updateTime;

    private Long authorId;
    private String authorName;
    private String authorAvatar;

}
