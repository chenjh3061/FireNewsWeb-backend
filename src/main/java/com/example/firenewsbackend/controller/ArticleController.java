package com.example.firenewsbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.dto.ArticleDTO;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 获取所有文章
     * @return Article
     */
    @GetMapping("/getAllArticles")
    public BaseResponse<List<ArticleDTO>> getAllArticles(){
        return ResultUtils.success(articleService.getAllArticles());
    }

    /**
     * 获取文章
     * @return Article
     */
    @GetMapping("/getArticleById")
    public BaseResponse<ArticleDTO> getArticleById(Integer id){
        return ResultUtils.success(articleService.getArticleById(id));
    }

    /**
     * 分页获取文章
     * @return Article
     */
    @GetMapping("/getArticlesByPage")
    public BaseResponse<Page<ArticleDTO>> getArticlesByPage(
            @RequestParam int pageNo,
            @RequestParam int pageSize){
        Page<ArticleDTO> articlesPage = articleService.getArticlesPage(pageNo, pageSize);
        return ResultUtils.success(articlesPage);
    }

    /**
     * 分页获取热点新闻
     * @return Article
     */
    @GetMapping("/getHotNewsByPage")
    public BaseResponse<Page<ArticleDTO>> getHotNewsByPage(
            @RequestParam int pageNo,
            @RequestParam int pageSize){
        Page<ArticleDTO> articlesPage = articleService.getHotNewsByPage(pageNo, pageSize);
        return ResultUtils.success(articlesPage);
    }


    /**
     * 获取作者文章
     * @return Article
     */
    @GetMapping("/getArticlesByAuthorId")
    public BaseResponse<List<ArticleDTO>> getArticlesByAuthorId(Integer id){
        return ResultUtils.success(articleService.getArticlesByAuthorId(id));
    }

    /**
     * 获取热点新闻
     * @return Article
     */
    @GetMapping("/getHotNews")
    public BaseResponse<List<ArticleDTO>> getHotNewsArticles() {
        List<ArticleDTO> hotNewsArticles = articleService.getHotNewsArticles();
        return ResultUtils.success(hotNewsArticles);
    }

    @GetMapping("/getHotScience")
    public BaseResponse<List<ArticleDTO>> getHotScienceArticles() {
        List<ArticleDTO> hotScienceArticles = articleService.getHotScienceArticles();
        return ResultUtils.success(hotScienceArticles);
    }

    /**
     * 获取轮播新闻
     * @return Article
     */
    @GetMapping("/getCarouselArticles")
    public BaseResponse<List<Article>> getCarouselArticles(){
        return ResultUtils.success(articleService.getCarouselArticles());
    }

    /**
     * 新增文章
     * @param article
     * @return Article
     */
    @PostMapping("/addArticle")
    public BaseResponse<Article> addArticle(Article article){
        return ResultUtils.success(articleService.addArticle(article));
    }

    /**
     * 更新文章
     * @param article
     * @return Article
     */
    @PostMapping("/updateArticle")
    public BaseResponse<Article> updateArticle(Article article){
        return ResultUtils.success(articleService.updateArticle(article));
    }

}
