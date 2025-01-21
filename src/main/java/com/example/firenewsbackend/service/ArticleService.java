package com.example.firenewsbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.eadao.ArticleEsDao;
import com.example.firenewsbackend.model.dto.ArticleDTO;
import com.example.firenewsbackend.model.dto.ArticleEsDTO;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.mapper.ArticleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleEsDao articleEsDao;

    public ArticleService(ArticleMapper articleMapper, ArticleEsDao articleEsDao) {
        this.articleMapper = articleMapper;
        this.articleEsDao = articleEsDao;
    }

    /**
     * 获取所有文章
     * @return Article
     */
    public List<ArticleDTO> getAllArticles() {
        return articleMapper.getAllArticles();
    }

    /**
     * 获取指定文章
     * @return Article
     */
    public ArticleDTO getArticleById(Long id){
        return articleMapper.getArticleById(id);
    }

    /**
     * 分页查询文章
     * @return Article
     */
    public Page<ArticleDTO> getArticlesPage(Integer pageNo, Integer pageSize){
        Page<ArticleDTO> page = new Page<>(pageNo, pageSize);
        return articleMapper.selectArticlesPage(page);
    }
    /**
     * 获取热点新闻
     * @return Article
     */
    public List<ArticleDTO> getHotNewsArticles() {
        return articleMapper.getHotArticlesByCategory(0);
    }


    /**
     * 获取科普热文
     * @return Article
     */
    public List<ArticleDTO> getHotScienceArticles() {
        // 科普文章 (articleCategory = 1)
        return articleMapper.getHotArticlesByCategory(1);
    }

    /**
     * 获取轮播新闻
     * @return Article
     */
    public List<Article> getCarouselArticles(){
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isCarousel", 1);
        queryWrapper.eq("reviewStatus", 1);
        queryWrapper.eq("isDelete", 0);
        return articleMapper.selectList(queryWrapper);
    }

    /**
     * 新增文章
     * @param article
     * @return Article
     */
    public Article addArticle(Article article){
        articleMapper.insert(article);
        return article;
    }

    /**
     * 更新文章
     *
     * @param article
     * @return Article
     */
    public ArticleDTO updateArticle(ArticleDTO article){
        // 获取当前登录用户角色
        // 如果是 writer 角色，则将文章的审核状态修改为 "未审核"（假设未审核是 0）
        if (StpUtil.hasRole("writer")) {
            article.setReviewStatus(0);  // 0 假设是未审核状态
        }
        articleMapper.updateById(article);
        return article;
    }

    /**
     * 删除文章
     * @param id
     * @return Article
     */
    public Article deleteArticle(Integer id){
        StpUtil.checkRole("admin");
        articleMapper.setIsDelete(id);
        return null;
    }

    /**
     * 获取作者文章
     * @param id
     * @return
     */
    public List<ArticleDTO> getArticlesByAuthorId(Integer id) {
        return articleMapper.getArticlesByAuthorId(id);
    }

    /**
     * 分页获取热门新闻
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<ArticleDTO> getHotNewsByPage(int pageNo, int pageSize) {
        Page<ArticleDTO> page = new Page<>(pageNo, pageSize);
        return articleMapper.getHotNewsByPage(page);
    }

    public Long getCarouselArticleCount() {
        // 查询当前数据库中已设置为轮播新闻的文章数量
        return articleMapper.selectCount(new QueryWrapper<Article>().eq("is_carousel", 1));
    }

    public List<ArticleDTO> getUnreviewedArticles() {
        return articleMapper.getUnreviewedArticles(new QueryWrapper<Article>().eq("review_status", 0));
    }

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    public ArticleDTO recordArticleView(Long articleId, Long userId) {
        logger.info("{\"timestamp\":\"" + System.currentTimeMillis() + "\",\"user_id\":\"" + userId + "\",\"action\":\"view_article\",\"article_id\":\"" + articleId + "\",\"article_title\":\"文章标题\",\"ip\":\"127.0.0.1\"}");
        ArticleDTO article = articleMapper.getArticleById(articleId);
        if (article == null) {
            logger.error("Article not found with id: " + articleId);
            // throw new RuntimeException("Article not found");
        }
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1L);
        }
        articleMapper.updateById(article);
        return article;
    }

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public List<ArticleDTO> searchArticle(String searchParams) {
        // 使用 Criteria 构建查询条件
        Criteria criteria = new Criteria();
        // 使用 contains 方法实现模糊搜索，也可以使用 wildcard 方法
        criteria = criteria.or("articleTitle").matches(searchParams)
                .or("articleContent").matches(searchParams);

        // 使用 CriteriaQuery 或 NativeSearchQueryBuilder 构建查询
        // 这里使用 CriteriaQuery
        CriteriaQuery searchQuery = new CriteriaQuery(criteria);
        // 设置分页
        searchQuery.setPageable(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createTime")));

        // 执行搜索操作
        SearchHits<ArticleEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, ArticleEsDTO.class);

        // 处理搜索结果
        List<ArticleDTO> result = new ArrayList<>();
        for (SearchHit<ArticleEsDTO> hit : searchHits) {
            ArticleEsDTO articleEsDTO = hit.getContent();
            // 这里假设你有一个方法可以将 ArticleEsDTO 转换为 ArticleDTO
            ArticleDTO articleDTO = ArticleEsDTO.dtoToObj(articleEsDTO);
            result.add(articleDTO);
        }

        return result;
    }

    public List<Article> getArticles() {
        return articleMapper.selectList(null);
    }
}
