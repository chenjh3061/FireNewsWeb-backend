package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.model.entity.Comments;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comments> {

    @Select("select " +
            "c.id as id, " +
            "c.articleId as articleId, " +
            "c.userId as userId, " +
            "c.parentCommentId as parentCommentId, " +
            "c.createTime as createTime, " +
            "c.updateTime as updateTime, " +
            "c.content as content, " +
            "c.likes as likes " +
            "from comments c " +
            "where c.articleId = #{id}")
    List<Comments> getAllCommentsByArticleId(Integer id);
}
