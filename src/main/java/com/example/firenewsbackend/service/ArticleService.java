package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.firenewsbackend.entity.Article;
import com.example.firenewsbackend.entity.User;
import com.example.firenewsbackend.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleService {

    @Resource
    private ArticleMapper  articleMapper;

    /**
     * 获取所有文章
     * @return Article
     */
    public List<Article> getAllUsers() {
        return articleMapper.selectList(null);
    }
}
