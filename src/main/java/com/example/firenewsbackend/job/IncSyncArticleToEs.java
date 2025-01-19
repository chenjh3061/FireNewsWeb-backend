package com.example.firenewsbackend.job;

import com.example.firenewsbackend.eadao.ArticleEsDao;
import com.example.firenewsbackend.mapper.ArticleMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.example.firenewsbackend.model.dto.ArticleEsDTO;
import com.example.firenewsbackend.model.entity.Article;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Incrementally synchronize articles to Elasticsearch
 */
// todo Uncomment to enable the task
@Component
@Slf4j
public class IncSyncArticleToEs {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleEsDao articleEsDao;

    /**
     * 每分钟执行一次增量同步
     * 同步过去 5 分钟内的数据
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询过去 5 分钟的文章数据
        Date fiveMinutesAgo = new Date(System.currentTimeMillis() - 5 * 60 * 1000L);
        List<Article> articleList = articleMapper.listArticleWithDelete(fiveMinutesAgo);

        if (CollUtil.isEmpty(articleList)) {
            log.info("No incremental articles found.");
            return;
        }

        List<ArticleEsDTO> articleEsDTOList = articleList.stream()
                .map(ArticleEsDTO::objToDto)
                .collect(Collectors.toList());

        final int pageSize = 500;
        int total = articleEsDTOList.size();
        log.info("Incremental sync start, total: {}", total);

        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("Syncing incremental articles from {} to {}", i, end);
            articleEsDao.saveAll(articleEsDTOList.subList(i, end));
        }

        log.info("Incremental sync finished, total: {}", total);
    }
}
