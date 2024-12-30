package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 获取评论数据
     * @return Comments
     */
    @GetMapping("/getAllComments")
    public BaseResponse<List<Comments>> getComments(){
        return ResultUtils.success(commentService.getAllComments());
    }

    @GetMapping("/getCommentsByArticleId")
    public BaseResponse<List<Comments>> getCommentsByArticleId(Integer id){
        return ResultUtils.success(commentService.getAllCommentsByArticleId(id));
    }

    @PostMapping("/addComment")
    public BaseResponse<Comments> addComment(Comments comment){
        return commentService.addComment(comment);
    }
}
