package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 获取评论数据
     * 1. 根据文章id获取评论
     * 2. 根据评论id获取子评论
     * 3. 根据评论id获取评论者信息
     */
    @GetMapping("/getComments")
    public void getComments(){

    }
}
