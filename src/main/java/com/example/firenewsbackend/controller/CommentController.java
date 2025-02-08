package com.example.firenewsbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.aop.LoggableOperation;
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

    @GetMapping("/getAllCommentsByPage")
    public BaseResponse<List<CommentsVO>> getAllCommentsByPage(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "20") int size){
        return ResultUtils.success(commentService.getAllCommentsByPage(page,size).getRecords());
    }

    @GetMapping("/getCommentsByArticleId")
    public BaseResponse<List<CommentsVO>> getCommentsByArticleId(@RequestParam Long id,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size){
        return ResultUtils.success(commentService.getAllCommentsByArticleId(id,page,size).getRecords());
    }

    @GetMapping("/searchCommentsByPage")
    public BaseResponse<List<CommentsVO>> getAllCommentsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {
        Page<CommentsVO> commentsVOPage = commentService.searchCommentsByPage(page, size, keyword);
        return ResultUtils.success(commentsVOPage.getRecords());
    }

    /**
     * 发表评论
     * @param comment
     * @return
     */
    @LoggableOperation(operationName = "发表评论", actionType = "comment", targetType = "comment")
    @PostMapping("/addComment")
    public BaseResponse<Comments> addComment(@RequestBody Comments comment){
        return ResultUtils.success(commentService.addComment(comment));
    }

    /**
     * 修改评论状态
     * @param id
     * @param status
     * @return
     */

    @PostMapping("/changeCommentStatus")
    public BaseResponse<CommentsVO> changeCommentStatus(@RequestParam Long id, @RequestParam Integer status){
        return ResultUtils.success(commentService.changeCommentStatus(id, status));
    }

    @DeleteMapping("/deleteCommentById")
    public BaseResponse<Integer> deleteCommentById(@RequestParam Long id){
        return ResultUtils.success(commentService.deleteCommentById(id));
    }
}
