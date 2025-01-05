package com.example.firenewsbackend.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginUserVO {

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

    private String Token;
}
