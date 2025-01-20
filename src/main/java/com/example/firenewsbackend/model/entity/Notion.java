package com.example.firenewsbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notion {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String userId;
    private String status;
    private String domain;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer  isDelete;

}
