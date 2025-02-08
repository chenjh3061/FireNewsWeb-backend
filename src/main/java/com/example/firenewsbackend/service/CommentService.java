package com.example.firenewsbackend.service;

import co.elastic.clients.elasticsearch.ml.ElasticsearchMlAsyncClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.mapper.ArticleMapper;
import com.example.firenewsbackend.mapper.CommentMapper;
import com.example.firenewsbackend.mapper.UserMapper;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.entity.User;
import com.example.firenewsbackend.model.vo.CommentsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService{

    @Resource
    private CommentMapper  commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ArticleMapper  articleMapper;

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

    // 软删除
    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public int deleteCommentById(Long id) {
        return commentMapper.setIsDelete(id);
    }

    /**
     * 分页查询评论
     *
     * @param page
     * @param size
     * @return
     */
    public Page<CommentsVO> getAllCommentsByPage(int page, int size) {
        Page<CommentsVO> pageParam = new Page<>(page, size);
        return commentMapper.getAllCommentsByPage(pageParam);
    }

    /**
     * 分页查询评论（包含文章和用户信息）
     *
     * @param page 页码
     * @param size 每页条数
     * @param keyword 搜索关键字，如果无搜索关键字则传入空字符串 ""
     * @return 分页后的 CommentsVO 数据
     */
    public Page<CommentsVO> searchCommentsByPage(int page, int size, String keyword) {
        // 当 keyword 为 null 时，转为空字符串，保证 SQL 正常执行
        if (keyword == null) {
            keyword = "";
        }
        // 创建分页对象，注意泛型为 CommentsVO
        Page<CommentsVO> voPage = new Page<>(page, size);
        // 调用 Mapper 自定义 SQL 查询
        Page<CommentsVO> resultPage = commentMapper.searchCommentsByPage(voPage, keyword);
        return resultPage;
    }
}
