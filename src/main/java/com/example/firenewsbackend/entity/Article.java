package com.example.firenewsbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("news")
public class Article {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String articleTitle;

    private String articleDesc; // 新闻简述

    private String articleAvatar; // 新闻封面

    private String articleCategory;

    private Integer reviewStatus; // 审核状态

    private String reviewMessage;

    private String content;

    private LocalDateTime  createTime;

    private LocalDateTime  updateTime;

    @TableLogic
    private Integer isDelete;

}

