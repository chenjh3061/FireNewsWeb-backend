package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.model.dto.ArticleDTO;
import com.example.firenewsbackend.model.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
            "a.isCarousel AS isCarousel, " +
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
            "a.isCarousel AS isCarousel, " +
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

    @Select("SELECT +" +
            "a.id AS articleId, " +
            "a.articleTitle AS articleTitle, " +
            "a.articleDesc AS articleDesc, " +
            "a.articleAvatar AS articleAvatar, " +
            "a.articleCategory AS articleCategory, " +
            "a.articleContent AS articleContent, " +
            "a.viewCount AS viewCount, " +
            "a.isCarousel AS isCarousel, " +
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
            "ORDER BY a.createTime DESC" )
    Page<ArticleDTO> selectArticlesPage(Page<ArticleDTO> page);

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
            "AND a.createTime >= DATE_SUB(NOW(), INTERVAL 20 DAY) " +  // 只显示过去7天的文章
            "ORDER BY a.viewCount DESC, a.createTime DESC " +
            "LIMIT 20")
    List<ArticleDTO> getHotArticlesByCategory(@Param("articleCategory") int articleCategory);


    @Select("SELECT " +
            "a.id AS articleId, " +
            "a.articleTitle AS articleTitle, " +
            "a.articleDesc AS articleDesc, " +
            "a.articleAvatar AS articleAvatar, " +
            "a.articleCategory AS articleCategory, " +
            "a.articleContent AS articleContent, " +
            "a.viewCount AS viewCount, " +
            "a.isCarousel AS isCarousel, " +
            "a.reviewStatus AS reviewStatus, " +
            "a.reviewMessage AS reviewMessage, " +
            "a.createTime AS createTime, " +
            "a.updateTime AS updateTime, " +
            "u.id AS authorId, " +
            "u.userName AS authorName, " +
            "u.userAvatar AS authorAvatar " +
            "FROM article a " +
            "LEFT JOIN user u ON a.authorId = u.id " +
            "WHERE a.authorId = #{authorId} " +
            "AND a.isDelete = 0 " +
            "AND a.reviewStatus = 1 " +
            "ORDER BY a.createTime DESC")
    List<ArticleDTO> getArticlesByAuthorId(Integer id);

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
            "AND a.articleCategory = 0 " +
            "ORDER BY a.viewCount DESC " +
            "LIMIT #{pageSize}")
    Page<ArticleDTO> getHotNewsByPage(Page<ArticleDTO> page);

    // 更新文章
    @Update("UPDATE article SET " +
            "articleTitle = #{articleTitle}, " +
            "articleDesc = #{articleDesc}, " +
            "articleAvatar = #{articleAvatar}, " +
            "articleCategory = #{articleCategory}, " +
            "articleContent = #{articleContent}, " +
            "isCarousel = #{isCarousel}, " +
            "reviewStatus = #{reviewStatus}, " +
            "reviewMessage = #{reviewMessage}, " +
            "updateTime = #{updateTime}, " +
            "authorId = #{authorId} " +
            "WHERE id = #{articleId} AND isDelete = 0")
    void updateById(ArticleDTO article);
}
