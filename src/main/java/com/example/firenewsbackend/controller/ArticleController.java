package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.entity.Article;
import com.example.firenewsbackend.service.ArticleService;
import io.swagger.v3.oas.models.links.Link;
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
    @RequestMapping("/getAllArticles")
    public BaseResponse<List<Article>> getAllArticles(){
        return ResultUtils.success(articleService.getAllUsers());
    }
}
