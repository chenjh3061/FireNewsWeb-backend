package com.example.firenewsbackend.repository;

import com.example.firenewsbackend.model.entity.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {
    @Query("{\"bool\":{\"should\":[{\"fuzzy\":{\"articleTitle\":{\"value\":\"?0\",\"fuzziness\":\"AUTO\"}}},{\"fuzzy\":{\"articleContent\":{\"value\":\"?1\",\"fuzziness\":\"AUTO\"}}}]}}")
    List<Article> findByTitleOrContent(String articleTitle, String articleContent);
}
