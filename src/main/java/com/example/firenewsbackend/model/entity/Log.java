package com.example.firenewsbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("log")
public class Log {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;          // 操作内容
    private LocalDateTime createTime;
    private String userAccount;
    private String actionType;
    private String targetType;
    private Long targetId;
    private String ip;
}
