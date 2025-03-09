package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.mapper.LogMapper;
import com.example.firenewsbackend.model.entity.Log;
import com.example.firenewsbackend.repository.LogEntry;
import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private static final int MAX_RETRIES = 3;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private LogMapper logMapper;

    // 记录操作日志
    public void logAction(String action, String userId, String description) {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                // 构造日志对象
                LogEntry logEntry = new LogEntry();
                logEntry.setAction(action);
                logEntry.setUserId(userId);
                logEntry.setDescription(description);
                logEntry.setTimestamp(LocalDateTime.now());

                // 将日志保存到 ElasticSearch
                elasticsearchRestTemplate.save(logEntry);
                break;
            } catch (ElasticsearchException e) {
                retryCount++;
                logger.error("Failed to save log entry to Elasticsearch (attempt {}): {}", retryCount, e.getMessage(), e);
                if (retryCount == MAX_RETRIES) {
                    logger.error("Reached maximum retries. Log entry not saved to Elasticsearch.");
                    // 可以考虑将日志保存到数据库等其他存储方式
                }
            }
        }
    }

    // 根据操作类型和用户ID统计日志
    public List<LogEntry> getLogsByUser(String userId) {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
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
            } catch (ElasticsearchException e) {
                retryCount++;
                logger.error("Failed to search logs in Elasticsearch by user (attempt {}): {}", retryCount, e.getMessage(), e);
                if (retryCount == MAX_RETRIES) {
                    logger.error("Reached maximum retries. Returning empty list.");
                    return new ArrayList<>();
                }
            }
        }
        return new ArrayList<>();
    }

    public List<Log> getRecentLog(int page, int size) {
        // 创建分页对象，传入当前页和每页记录数
        Page<Log> pageRequest = new Page<>(page, size);

        // 调用 Mapper 进行分页查询
        Page<Log> result = logMapper.findRecentLogs(pageRequest);

        // 返回查询结果
        return result.getRecords();
    }
}