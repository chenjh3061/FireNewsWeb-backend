package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.vo.CommentsVO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comments> {

    @Select("select " +
            "c.id as id, " +
            "c.articleId as articleId, " +
            "a.articleTitle as articleTitle, " +
            "c.userId as userId, " +
            "u.userName as userName, " +
            "u.userAvatar as userAvatar, " +
            "c.parentCommentId as parentCommentId, " +
            "c.createTime as createTime, " +
            "c.updateTime as updateTime, " +
            "c.content as content, " +
            "c.likes as likes " +
            "from comments c " +
            "left join article a on c.articleId = a.id " +
            "left join user u on c.userId = u.id " +
            "where c.isDelete = 0 " +
            "and c.isShow = 1 " +
            "and c.articleId = #{articleId} " +
            "order by c.createTime desc")
    List<CommentsVO> getAllCommentsByArticleId(Long id);

    @Select(
            "select " +
                    "c.id as id, " +
                    "c.articleId as articleId, " +
                    "a.articleTitle as articleTitle, " +
                    "c.userId as userId, " +
                    "u.userName as userName, " +
                    "u.userAvatar as userAvatar, " +
                    "c.parentCommentId as parentCommentId, " +
                    "c.createTime as createTime, " +
                    "c.updateTime as updateTime, " +
                    "c.content as content, " +
                    "c.likes as likes, " +
                    "c.isShow as isShow " +
                    "from comments c " +
                    "left join article a on c.articleId = a.id " +
                    "left join user u on c.userId = u.id " +
                    "where c.isDelete = 0 " +
                    "order by c.createTime desc"
    )
    List<CommentsVO> getAllComments();

    @Update("UPDATE comments " +
            "SET isShow = #{status} " +
            "WHERE id = #{id}")
    int changeCommentStatus(Long id, Integer status);

    @Select("select " +
            "c.id as id, " +
            "c.articleId as articleId, " +
            "a.articleTitle as articleTitle, " +
            "c.userId as userId, " +
            "u.userName as userName, " +
            "u.userAvatar as userAvatar, " +
            "c.parentCommentId as parentCommentId, " +
            "c.createTime as createTime, " +
            "c.updateTime as updateTime, " +
            "c.content as content, " +
            "c.likes as likes " +
            "from comments c " +
            "left join article a on c.articleId = a.id " +
            "left join user u on c.userId = u.id " +
            "where c.isDelete = 0 " +
            "and c.isShow = 1 " +
            "and c.id = #{id}")
    CommentsVO getCommentById(Long id);
}
