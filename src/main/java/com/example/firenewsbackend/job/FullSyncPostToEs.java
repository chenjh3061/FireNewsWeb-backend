package com.example.firenewsbackend.job;

import cn.hutool.core.collection.CollUtil;
import com.example.firenewsbackend.eadao.ArticleEsDao;
import com.example.firenewsbackend.model.dto.ArticleEsDTO;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全量同步帖子到 es
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleEsDao articleEsDao;

    @Override
    public void run(String... args) {
        List<Article> articleList = articleService.getArticles();
        if (CollUtil.isEmpty(articleList)) {
            return;
        }
        List<ArticleEsDTO> postEsDTOList = articleList.stream().map(ArticleEsDTO::objToDto).collect(Collectors.toList());
        final int pageSize = 500;
        int total = postEsDTOList.size();
        log.info("FullSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            articleEsDao.saveAll(postEsDTOList.subList(i, end));
        }
        log.info("FullSyncPostToEs end, total {}", total);
    }
}
