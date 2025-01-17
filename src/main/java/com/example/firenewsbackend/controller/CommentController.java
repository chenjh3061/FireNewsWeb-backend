package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.vo.CommentsVO;
import com.example.firenewsbackend.service.CommentService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<List<CommentsVO>> getAllComments(){
        return ResultUtils.success(commentService.getAllComments());
    }

    @GetMapping("/getCommentsByArticleId")
    public BaseResponse<List<CommentsVO>> getCommentsByArticleId(Long id){
        return ResultUtils.success(commentService.getAllCommentsByArticleId(id));
    }

    @PostMapping("/addComment")
    public BaseResponse<Comments> addComment(Comments comment){
        return ResultUtils.success(commentService.addComment(comment).getData());
    }

    @PostMapping("/changeCommentStatus")
    public BaseResponse<CommentsVO> changeCommentStatus(@RequestParam Long id, @RequestParam Integer status){
        return ResultUtils.success(commentService.changeCommentStatus(id, status));
    }
}
