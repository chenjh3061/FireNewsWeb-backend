package com.example.firenewsbackend.service;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.mapper.CommentMapper;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.vo.CommentsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService{

    @Resource
    private CommentMapper  commentMapper;

    public List<CommentsVO> getAllComments() {
        return commentMapper.getAllComments();
    }

    public List<CommentsVO> getAllCommentsByArticleId(Long id) {
        return commentMapper.getAllCommentsByArticleId(id);
    }

    public BaseResponse<Comments> addComment(Comments comment) {
        commentMapper.insert(comment);
        return null;
    }

    public CommentsVO changeCommentStatus(Long id, Integer status) {
        int updateRows = commentMapper.changeCommentStatus(id, status);
        CommentsVO commentsVO = commentMapper.getCommentById(id);
        return commentsVO;
    }
}
