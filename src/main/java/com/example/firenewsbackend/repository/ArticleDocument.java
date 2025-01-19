package com.example.firenewsbackend.repository;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Document(indexName = "article")
@Data
public class ArticleDocument {

    @Id
    private String id;
    private String articleTitle;
    private String articleContent;
    private String authorName;
    private String authorId;
    private String authorAvatar;
    private String articleCategory;
    private String articleAvatar;
    private String articleDesc;
    private String articleTags;
    private LocalDateTime createDate;

    // Getters and Setters
}

