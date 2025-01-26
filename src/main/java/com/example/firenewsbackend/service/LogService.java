package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.LogMapper;
import com.example.firenewsbackend.model.entity.Log;
import com.example.firenewsbackend.repository.LogEntry;
import org.elasticsearch.index.similarity.ScriptedSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private LogMapper logMapper;

    // 记录操作日志
    public void logAction(String action, String userId, String description) {
        // 构造日志对象
        LogEntry logEntry = new LogEntry();
        logEntry.setAction(action);
        logEntry.setUserId(userId);
        logEntry.setDescription(description);
        logEntry.setTimestamp(LocalDateTime.now());

        // 将日志保存到 ElasticSearch
        elasticsearchRestTemplate.save(logEntry);
    }

    // 根据操作类型和用户ID统计日志
    public List<LogEntry> getLogsByUser(String userId) {
        Criteria criteria = new Criteria("userId").is(userId);

        // 构建 CriteriaQuery
        CriteriaQuery searchQuery = new CriteriaQuery(criteria);

        // 设置分页和排序，例如按时间倒序
        searchQuery.setPageable(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "timestamp")));

        // 执行搜索操作
        SearchHits<LogEntry> searchHits = elasticsearchRestTemplate.search(searchQuery, LogEntry.class);

        // 处理搜索结果
        List<LogEntry> result = new ArrayList<>();
        for (SearchHit<LogEntry> hit : searchHits) {
            result.add(hit.getContent());
        }

        return result;
    }

    public List<Log> getRecentLog(int limit) {
        return logMapper.findRecentLogs(limit);
    }
}

