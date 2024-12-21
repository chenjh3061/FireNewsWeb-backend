package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.firenewsbackend.model.dto.ArticleDTO;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 获取所有文章
     * @return Article
     */
    public List<ArticleDTO> getAllArticles() {
        return articleMapper.getAllArticles();
    }

    /**
     * 获取文章
     * @return Article
     */
    public ArticleDTO getArticleById(Integer id){
        return articleMapper.getArticleById(Long.valueOf(id));
    }

    /**
     * 获取热点新闻
     * @return Article
     */
    public List<ArticleDTO> getHotNewsArticles() {
        return articleMapper.getHotArticlesByCategory(0);
    }


    /**
     * 获取科普热文
     * @return Article
     */
    public List<ArticleDTO> getHotScienceArticles() {
        // 科普文章 (articleCategory = 1)
        return articleMapper.getHotArticlesByCategory(1);
    }

    /**
     * 获取轮播新闻
     * @return Article
     */
    public List<Article> getCarouselArticles(){
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isCarousel", 1);
        return articleMapper.selectList(queryWrapper);
    }

    /**
     * 新增文章
     * @param article
     * @return Article
     */
    public Article addArticle(Article article){
        articleMapper.insert(article);
        return article;
    }

    /**
     * 更新文章
     * @param article
     * @return Article
     */
    public Article updateArticle(Article article){
        articleMapper.updateById(article);
        return article;
    }

    /**
     * 删除文章
     * @param id
     * @return Article
     */
    public Article deleteArticle(Integer id){
        articleMapper.deleteById(id);
        return null;
    }


}
