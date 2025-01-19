package com.example.firenewsbackend.eadao;

import com.example.firenewsbackend.model.dto.ArticleEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface ArticleEsDao extends ElasticsearchRepository<ArticleEsDTO, Long> {

    List<ArticleEsDTO> findByAuthorId(Long userId);

    List<ArticleEsDTO> findByarticleTitle(String title);
}
