package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.dto.ArticleDTO;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 获取热点新闻
     * @return Article
     */
    @GetMapping("/getHotNews")
    public ResponseEntity<List<ArticleDTO>> getHotNewsArticles() {
        List<ArticleDTO> hotNewsArticles = articleService.getHotNewsArticles();
        return ResponseEntity.ok(hotNewsArticles);
    }

    @GetMapping("/getHotScience")
    public ResponseEntity<List<ArticleDTO>> getHotScienceArticles() {
        List<ArticleDTO> hotScienceArticles = articleService.getHotScienceArticles();
        return ResponseEntity.ok(hotScienceArticles);
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
