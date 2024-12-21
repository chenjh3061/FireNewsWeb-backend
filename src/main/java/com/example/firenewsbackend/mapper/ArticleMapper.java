package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.model.dto.ArticleDTO;
import com.example.firenewsbackend.model.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT " +
            "a.id AS articleId, " +
            "a.articleTitle AS articleTitle, " +
            "a.articleDesc AS articleDesc, " +
            "a.articleAvatar AS articleAvatar, " +
            "a.articleCategory AS articleCategory, " +
            "a.articleContent AS articleContent, " +
            "a.viewCount AS viewCount, " +
            "a.reviewStatus AS reviewStatus, " +
            "a.reviewMessage AS reviewMessage, " +
            "a.createTime AS createTime, " +
            "a.updateTime AS updateTime, " +
            "u.id AS authorId, " +
            "u.userName AS authorName, " +
            "u.userAvatar AS authorAvatar " +
            "FROM article a " +
            "LEFT JOIN user u ON a.authorId = u.id " +
            "WHERE a.isDelete = 0 " +
            "AND a.reviewStatus = 1 " +
            "ORDER BY a.createTime DESC")  // 根据创建时间降序排序
    List<ArticleDTO> getAllArticles();

    @Select("SELECT " +
            "a.id AS articleId, " +
            "a.articleTitle AS articleTitle, " +
            "a.articleDesc AS articleDesc, " +
            "a.articleAvatar AS articleAvatar, " +
            "a.articleCategory AS articleCategory, " +
            "a.articleContent AS articleContent, " +
            "a.viewCount AS viewCount, " +
            "a.reviewStatus AS reviewStatus, " +
            "a.reviewMessage AS reviewMessage, " +
            "a.createTime AS createTime, " +
            "a.updateTime AS updateTime, " +
            "u.id AS authorId, " +
            "u.userName AS authorName, " +
            "u.userAvatar AS authorAvatar " +
            "FROM article a " +
            "LEFT JOIN user u ON a.authorId = u.id " +
            "WHERE a.id = #{articleId} " +  // 根据文章ID查找
            "AND a.isDelete = 0 " +         // 确保文章未删除
            "AND a.reviewStatus = 1")       // 只返回审核通过的文章
    ArticleDTO getArticleById(@Param("articleId") Long articleId);


    @Select("SELECT " +
            "a.id AS articleId, " +
            "a.articleTitle AS articleTitle, " +
            "a.articleDesc AS articleDesc, " +
            "a.articleAvatar AS articleAvatar, " +
            "a.articleCategory AS articleCategory, " +
            "a.articleContent AS articleContent, " +
            "a.viewCount AS viewCount, " +
            "a.reviewStatus AS reviewStatus, " +  // 新增字段
            "a.reviewMessage AS reviewMessage, " + // 新增字段
            "a.createTime AS createTime, " +
            "a.updateTime AS updateTime, " + // 新增字段
            "u.id AS authorId, " +
            "u.userName AS authorName, " +
            "u.userAvatar AS authorAvatar " +
            "FROM article a " +
            "LEFT JOIN user u ON a.authorId = u.id " +
            "WHERE a.articleCategory = #{articleCategory} " +
            "AND a.isDelete = 0 " +
            "AND a.reviewStatus = 1 " +
            "ORDER BY a.viewCount DESC " +
            "LIMIT 10")
    List<ArticleDTO> getHotArticlesByCategory(@Param("articleCategory") int articleCategory);




}
