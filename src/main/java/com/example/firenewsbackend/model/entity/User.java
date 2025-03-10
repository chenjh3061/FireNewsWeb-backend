package com.example.firenewsbackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    // admin 123

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String userAccount;

    private String userPassword;

    private String unionId; // 微信开放平台ID

    private String mpOpenId; // 微信公众号ID

    private String userName; // 用户昵称

    private String userAvatar; // 用户头像

    private String userProfile; // 用户简介

    private String userRole; // "user" / "admin" / "writer" / "ban"

    private String email; // 邮箱

    private LocalDateTime  createTime;

    private LocalDateTime  updateTime;

    @TableLogic
    private Integer isDelete;

}
