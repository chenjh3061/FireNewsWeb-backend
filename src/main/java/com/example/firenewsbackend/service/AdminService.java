package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.AdminMapper;
import com.example.firenewsbackend.mapper.ArticleMapper;
import com.example.firenewsbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserMapper userMapper;

    public Long getUserNum() {
        return userMapper.selectCount(null);
    }

    public Long getArticleNum() {
        return articleMapper.selectCount(null);
    }

    public Long getTodayViewCount() {
        return null;
    }

    public Long getRunningDays() {

        return null;
    }

    public void resetPassword(String userId) {
        String password = "123456";
        adminMapper.resetPassword(userId, password);
    }
}
