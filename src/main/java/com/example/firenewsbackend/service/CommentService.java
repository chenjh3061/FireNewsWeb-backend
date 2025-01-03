package com.example.firenewsbackend.service;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.mapper.CommentMapper;
import com.example.firenewsbackend.model.entity.Comments;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService{

    @Resource
    private CommentMapper  commentMapper;

    public List<Comments> getAllComments() {
        return commentMapper.selectList(null);
    }

    public List<Comments> getAllCommentsByArticleId(Long id) {
        return commentMapper.getAllCommentsByArticleId(id);
    }

    public BaseResponse<Comments> addComment(Comments comment) {
        commentMapper.insert(comment);
        return null;
    }
}
