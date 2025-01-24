package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.mapper.CommentMapper;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.vo.CommentsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService{

    @Resource
    private CommentMapper  commentMapper;

    public List<CommentsVO> getAllComments() {
        return commentMapper.getAllComments();
    }

    public IPage<CommentsVO> getAllCommentsByArticleId(Long id, int page, int size) {
        Page<CommentsVO> pageParam = new Page<>(page, size);
        return commentMapper.getAllCommentsByArticleId(pageParam,id);
    }

    public Comments addComment(Comments comment) {
        System.out.println(comment);
        comment.setCreateTime(LocalDateTime.now());
        commentMapper.insert(comment);
        return comment;
    }

    public CommentsVO changeCommentStatus(Long id, Integer status) {
        int updateRows = commentMapper.changeCommentStatus(id, status);
        CommentsVO commentsVO = commentMapper.getCommentById(id);
        return commentsVO;
    }
}
