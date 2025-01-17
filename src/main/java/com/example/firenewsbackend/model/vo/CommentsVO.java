package com.example.firenewsbackend.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsVO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long articleId;

    private String articleTitle;

    private Long userId;

    private String userName;

    private String userAvatar;

    private Long parentCommentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String content; // 评论内容

    private Integer likes; // 点赞数

    private Integer isShow;

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}
