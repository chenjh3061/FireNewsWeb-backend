package com.example.firenewsbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("log")
public class Log {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private String createTime;
    private String userAccount;
    private String ip;
}
