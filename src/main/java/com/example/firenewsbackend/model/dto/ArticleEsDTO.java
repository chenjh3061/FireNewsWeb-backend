package com.example.firenewsbackend.model.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.example.firenewsbackend.model.entity.Article;
import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 帖子 ES 包装类
 **/
// todo 取消注释开启 ES（须先配置 ES）
@Document(indexName = "article")
@Data
public class ArticleEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Id
    private Long articleId;   // 文章ID

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String articleTitle;   // 文章标题
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String articleContent; // 文章内容
    private String articleDesc;    // 文章简介
    private String articleAvatar;  // 文章封面
    private Integer articleCategory; // 分类

    private List<String> tags;  // 标签列表
    private Long viewCount;     // 浏览量
    private Long authorId;      // 作者ID
    private String authorName;  // 作者姓名
    private String authorAvatar;// 作者头像

    @Field(index = false, store = true, type = FieldType.Date, pattern = DATE_TIME_PATTERN)
    private Date createTime;    // 创建时间

    @Field(index = false, store = true, type = FieldType.Date, pattern = DATE_TIME_PATTERN)
    private Date updateTime;    // 更新时间

    private Integer isDelete;   // 是否删除

    // 转换方法
    public static ArticleEsDTO objToDto(Article article) {
        if (article == null) {
            return null;
        }
        ArticleEsDTO articleEsDTO = new ArticleEsDTO();
        // 手动复制字段，如果 BeanUtils 未能正确处理
        articleEsDTO.setArticleId(article.getId());
        articleEsDTO.setArticleTitle(article.getArticleTitle());
        articleEsDTO.setArticleContent(article.getArticleContent());
        // 其他字段类似处理
        if (article.getTags() != null) {
            articleEsDTO.setTags(Arrays.asList(article.getTags().split(",")));
        }
        return articleEsDTO;
    }


    public static ArticleDTO dtoToObj(ArticleEsDTO articleEsDTO) {
        if (articleEsDTO == null) {
            return null;
        }
        ArticleDTO article = new ArticleDTO();
        BeanUtils.copyProperties(articleEsDTO, article);
        if (CollUtil.isNotEmpty(articleEsDTO.getTags())) {
            article.setTags(StringUtils.join(articleEsDTO.getTags(), ","));
        }
        return article;
    }

}

