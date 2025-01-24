package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.vo.CommentsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comments> {

    @Select("SELECT " +
            "c.id AS id, " +
            "c.articleId AS articleId, " +
            "a.articleTitle AS articleTitle, " +
            "c.userId AS userId, " +
            "u.userName AS userName, " +
            "u.userAvatar AS userAvatar, " +
            "c.parentCommentId AS parentCommentId, " +
            "c.createTime AS createTime, " +
            "c.updateTime AS updateTime, " +
            "c.content AS content, " +
            "c.likes AS likes " +
            "FROM comments c " +
            "LEFT JOIN article a ON c.articleId = a.id " +
            "LEFT JOIN user u ON c.userId = u.id " +
            "WHERE c.isDelete = 0 " +
            "AND c.isShow = 1 " +
            "AND c.articleId = #{articleId} " +
            "ORDER BY c.createTime DESC")
    IPage<CommentsVO> getAllCommentsByArticleId(Page<?> page, @Param("articleId") Long id);


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
