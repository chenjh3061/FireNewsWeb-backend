package com.example.firenewsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.aop.LoggableOperation;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.job.IncSyncArticleToEs;
import com.example.firenewsbackend.model.dto.ArticleDTO;
import com.example.firenewsbackend.model.dto.article.ArticleReviewRequest;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;
    private String searchParams;

    /**
     * 获取所有文章
     * @return Article
     */
    @GetMapping("/getAllArticles")
    public BaseResponse<List<ArticleDTO>> getAllArticles(){
        return ResultUtils.success(articleService.getAllArticles());
    }

    /**
     * 获取文章
     * @return Article
     */
    @GetMapping("/getArticleById")
    public BaseResponse<ArticleDTO> getArticleById(Integer id){
        return ResultUtils.success(articleService.getArticleById(Long.valueOf(id)));
    }

    /**
     * 分页获取文章
     * @return Article
     */
    @GetMapping("/getArticlesByPage")
    public BaseResponse<Page<ArticleDTO>> getArticlesByPage(
            @RequestParam int pageNo,
            @RequestParam int pageSize){
        Page<ArticleDTO> articlesPage = articleService.getArticlesPage(pageNo, pageSize);
        return ResultUtils.success(articlesPage);
    }

    /**
     * 分页获取热点新闻
     * @return Article
     */
    @GetMapping("/getHotNewsByPage")
    public BaseResponse<Page<ArticleDTO>> getHotNewsByPage(
            @RequestParam int pageNo,
            @RequestParam int pageSize){
        Page<ArticleDTO> articlesPage = articleService.getHotNewsByPage(pageNo, pageSize);
        return ResultUtils.success(articlesPage);
    }

    @GetMapping("/searchArticle")
    public BaseResponse<Object> searchArticle(@RequestParam String searchParams,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        return ResultUtils.success(articleService.searchArticle(searchParams, page, size), articleService.total);
    }
    @GetMapping("/getTotalSearchResults")
    public BaseResponse<Long> getTotalSearchResults() {
        return ResultUtils.success(articleService.total);
    }


    /**
     * 获取作者文章
     * @return Article
     */
    @GetMapping("/getArticlesByAuthorId")
    public BaseResponse<List<ArticleDTO>> getArticlesByAuthorId(Integer id){
        return ResultUtils.success(articleService.getArticlesByAuthorId(id));
    }

    /**
     * 获取热点新闻
     * @return Article
     */
    @GetMapping("/getHotNews")
    public BaseResponse<List<ArticleDTO>> getHotNewsArticles() {
        List<ArticleDTO> hotNewsArticles = articleService.getHotNewsArticles();
        return ResultUtils.success(hotNewsArticles);
    }

    @GetMapping("/getHotScience")
    public BaseResponse<List<ArticleDTO>> getHotScienceArticles() {
        List<ArticleDTO> hotScienceArticles = articleService.getHotScienceArticles();
        return ResultUtils.success(hotScienceArticles);
    }

    /**
     * 获取轮播新闻
     * @return Article
     */
    @GetMapping("/getCarouselArticles")
    public BaseResponse<List<Article>> getCarouselArticles(){
        return ResultUtils.success(articleService.getCarouselArticles());
    }

    /**
     * 设置轮播新闻
     * @param id 文章ID
     * @return 更新后的文章
     */
    @PostMapping("/setCarouselArticles")
    @SaCheckRole("admin")
    public BaseResponse<ArticleDTO> setCarouselArticles(@RequestParam Long id) {
        // 获取文章信息
        ArticleDTO article = articleService.getArticleById(id);

        // 判断文章是否存在
        if (article == null) {
            ResultUtils.error(ErrorCode.NOT_FOUND_ERROR.getCode(),"文章未找到");
        }

        // 控制数量
        Long carouselCount = articleService.getCarouselArticleCount();
        if (carouselCount >= 5) {
           return (BaseResponse<ArticleDTO>) ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(),"轮播新闻数量已达上限");
        }

        // 设置轮播新闻标识
        if (article != null) {
            article.setIsCarousel(1);  // 设置为轮播新闻
        }

        // 更新数据库
        articleService.updateArticle(article);  // 调用文章服务的更新方法

        // 返回更新后的文章
        return ResultUtils.success(article);
    }


    /**
     * 取消轮播新闻
     * @param id 文章ID
     * @return 更新后的文章
     */
    @PostMapping("/cancelCarouselArticles")
    @SaCheckRole("admin")
    public BaseResponse<ArticleDTO> cancelCarouselArticles(@RequestParam Long id) {
        // 获取文章信息
        ArticleDTO article = articleService.getArticleById(id);

        if (article == null) {
            // 文章不存在，返回 404 错误
            ResultUtils.error(ErrorCode.NOT_FOUND_ERROR.getCode(),"文章未找到");
        }

        // 取消轮播新闻标识
        if (article != null) {
            article.setIsCarousel(0);  // 设置为非轮播新闻
        }

        // 更新数据库
        articleService.updateArticle(article);  // 调用文章服务的更新方法

        // 返回更新后的文章
        return ResultUtils.success(article);
    }


    /**
     * 新增文章
     * @param article
     * @return Article
     */
    @LoggableOperation(operationName = "发布文章", actionType = "publish", targetType = "article")
    @PostMapping("/addArticle")
    public BaseResponse<Article> addArticle(@RequestBody Article article){
        StpUtil.checkRoleOr("admin","writer");
        return ResultUtils.success(articleService.addArticle(article));
    }

    /**
     * 更新文章
     * @return Article
     */
    @LoggableOperation(operationName = "编辑文章", actionType = "edit", targetType = "article")
    @PostMapping("/updateArticle")
    public BaseResponse<ArticleDTO> updateArticle(@RequestBody ArticleDTO articleDTO){
        StpUtil.checkRoleOr("admin","writer");
        return ResultUtils.success(articleService.updateArticle(articleDTO));
    }

    /**
     * 获取未审核文章
     */
    @GetMapping("/getUnreviewedArticles")
    public BaseResponse<List<ArticleDTO>> getUnreviewedArticles(){
        return ResultUtils.success(articleService.getUnreviewedArticles());
    }

    /**
     * 审核文章
     */
    @LoggableOperation(operationName = "审核文章", actionType = "review", targetType = "article")
    @PostMapping("/reviewArticle")
    public BaseResponse<ArticleDTO> reviewArticle(@RequestBody ArticleReviewRequest request){
        StpUtil.checkRole("admin");
        Long id = request.getId();
        Integer reviewStatus = request.getReviewStatus();
        String reviewMessage = request.getReviewMessage();
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            throw  new RuntimeException(id + "文章未找到");
        }
        article.setReviewStatus(reviewStatus);
        article.setReviewMessage(reviewMessage);
        Long adminId = StpUtil.getLoginIdAsLong();
        article.setReviewerId(adminId);
        System.out.println("文章："+article);
        return ResultUtils.success(articleService.updateArticle(article));
    }

    /**
     * 删除文章
     * @param id
     * @return Article
     */
    @LoggableOperation(operationName = "删除文章", actionType = "delete", targetType = "article")
    @PostMapping("/deleteArticle")
    public BaseResponse<Article> deleteArticle(Integer id){
        StpUtil.checkRole("admin");
        return ResultUtils.success(articleService.deleteArticle(id));
    }

    /**
     * 记录用户浏览
     */
    @PostMapping("/recordArticleView")
    public BaseResponse<ArticleDTO> recordArticleView(@RequestParam Long articleId, @RequestParam Long userId) {
        StpUtil.checkLogin();
        return ResultUtils.success(articleService.recordArticleView(articleId, userId));
    }

    /**
     * 统计近七天的文章浏览量和发布情况
     */
    @GetMapping("/getArticleStatsForLastSevenDays")
    public BaseResponse<List<ArticleDTO>> getArticleStatsForLastSevenDays() {
        List<ArticleDTO> articleStats = articleService.getArticleStatsForLastSevenDays();
        return ResultUtils.success(articleStats);
    }

}
