package com.example.firenewsbackend.model.dto.article;

import lombok.Data;

@Data
public class ArticleReviewRequest {
    private Long id;
    private Integer reviewStatus;
    private String reviewMessage;
}
