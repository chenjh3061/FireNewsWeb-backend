package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public ArticleDTO getArticleById(Long id){
        return articleMapper.getArticleById(id);
    }

    /**
     * 分页查询文章
     * @return Article
     */
    public Page<ArticleDTO> getArticlesPage(Integer pageNo, Integer pageSize){
        Page<ArticleDTO> page = new Page<>(pageNo, pageSize);
        return articleMapper.selectArticlesPage(page);
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
     *
     * @param article
     * @return Article
     */
    public ArticleDTO updateArticle(ArticleDTO article){
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


    public List<ArticleDTO> getArticlesByAuthorId(Integer id) {
        return articleMapper.getArticlesByAuthorId(id);
    }

    public Page<ArticleDTO> getHotNewsByPage(int pageNo, int pageSize) {
        Page<ArticleDTO> page = new Page<>(pageNo, pageSize);
        return articleMapper.getHotNewsByPage(page);
    }

    public Long getCarouselArticleCount() {
        // 查询当前数据库中已设置为轮播新闻的文章数量
        return articleMapper.selectCount(new QueryWrapper<Article>().eq("is_carousel", 1));
    }
}
