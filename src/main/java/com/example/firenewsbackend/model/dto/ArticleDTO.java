package com.example.firenewsbackend.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleDTO {
    @TableField("id")
    private Long articleId;
    private String articleTitle;
    private String articleDesc;
    private String articleAvatar;
    private Integer articleCategory;
    private String articleContent;
    private Long viewCount = 0L;

    private Integer reviewStatus;
    private String reviewMessage;
    private Long reviewerId;
    private Integer isCarousel;
    private LocalDateTime createTime;
    private LocalDateTime  updateTime;

    private Long authorId;
    private String authorName;
    private String authorAvatar;

    public void setIsCarousel(int isCarousel) {
        this.isCarousel = isCarousel;
    }

    public void setTags(String join) {
    }
}
