package com.example.firenewsbackend.aop;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.mapper.LogMapper;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.entity.Log;
import com.example.firenewsbackend.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Aspect
@Component
@Slf4j
@Order(1) // 值越大，优先级越低
public class LogInterceptor {

    @Resource
    private LogMapper logMapper;

    private static final int MAX_NAME_LENGTH = 155;

    /**
     * 执行拦截
     */
    @Around("execution(* com.example.firenewsbackend.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 获取请求路径、请求方式、请求IP
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod(); // 请求方法
        String clientIP = httpServletRequest.getRemoteHost(); // 请求IP

        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";

        // 输出请求日志
        log.info("request start, id: {}, path: {}, method: {}, ip: {}, params: {}", requestId, url, method,
                clientIP, reqParam);

        // 创建日志记录对象
        Log logRecord = new Log();
        logRecord.setRequestMethod(method);
        logRecord.setRequestPath(url);
        logRecord.setRequestIP(clientIP);
        logRecord.setParams(reqParam);

        // 给 name 字段赋值，避免插入时为 null
        logRecord.setName("Request: " + method + " " + url);

        // 获取用户信息
        String userAccount = StpUtil.isLogin() ? StpUtil.getTokenSession().get("UserAccount").toString() : "未登录";
        logRecord.setUserAccount(userAccount);

        // 执行原方法
        Object result = point.proceed();

        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, path: {}, cost: {}ms", requestId, url, totalTimeMillis);

        return result;
    }

    /**
     * 新增注解逻辑：拦截带有 @LoggableOperation 注解的方法
     */
    @Around("@annotation(loggableOperation)")
    public Object logOperation(ProceedingJoinPoint point, LoggableOperation loggableOperation) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 获取注解内容
        String operationName = loggableOperation.operationName();
        String actionType = loggableOperation.actionType();
        String targetType = loggableOperation.targetType();

        // 获取用户信息和请求参数
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取用户信息
        String userAccount = StpUtil.isLogin() ? StpUtil.getTokenSession().get("UserAccount").toString() : "未登录";

        Object[] args = point.getArgs();

        // 记录日志
        Log logRecord = new Log();
        String targetInfo = getTargetInfo(targetType, args);
        if (targetInfo.length() > MAX_NAME_LENGTH) {
            targetInfo = targetInfo.substring(0, MAX_NAME_LENGTH);
        }
        logRecord.setName(operationName + "-" + targetInfo);
        logRecord.setUserAccount(userAccount);
        logRecord.setCreateTime((LocalDateTime.now()));
        logRecord.setActionType(actionType);
        logRecord.setTargetType(targetType);
        logRecord.setTargetId(getTargetId(targetType, args));

        // 额外的请求相关信息
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logRecord.setRequestMethod(httpServletRequest.getMethod());
        logRecord.setRequestPath(httpServletRequest.getRequestURI());
        logRecord.setRequestIP(httpServletRequest.getRemoteHost());
        logRecord.setParams(Arrays.toString(args));

        logMapper.insert(logRecord);

        Object result = point.proceed();

        stopWatch.stop();
        log.info("Annotated request completed, operation: {}, duration: {}ms", operationName, stopWatch.getTotalTimeMillis());
        return result;
    }

    /**
     * 获取目标ID
     */
    private Long getTargetId(String url, Object[] args) {
        System.out.println("获取目标参数: " + Arrays.toString(args));
        // 通过URL和参数来获取目标ID（如文章ID、评论ID等）
        if (url.contains("/article")) {
            // 如果是新增文章操作，目标ID为文章的ID
            return (((Article) args[0]).getId());  // 假设args[0]是Article对象
        } else if (url.contains("/comment")) {
            // 如果是评论操作，目标ID为评论的ID或文章ID
            Comments comment = (Comments) args[0];
            System.out.println(comment.getArticleId());
            return (comment.getArticleId());  // 假设args[0]是Comments对象，可以根据需要修改
        } else if (url.contains("/user")) {
            // 如果是用户操作，目标ID为用户ID
            User user = (User) args[0];
            return (user.getId());  // 假设args[0]是User对象，可以根据需要修改
        } else if (url.contains("/admin")) {
            return Long.valueOf("admin");
        }
        return null;
    }

    /**
     * 获取目标信息（如文章标题、评论内容等）
     */
    private String getTargetInfo(String targetType, Object[] args) {
        if (targetType.equals("article") && args[0] instanceof Article) {
            Article article = (Article) args[0];
            if (article.getArticleTitle() != null) {
                return article.getArticleTitle(); // 返回文章标题
            } else {
                return article.getId().toString();
            }
        } else if (targetType.equals("comment") && args[0] instanceof Comments) {
            Comments comment = (Comments) args[0];
            return comment.getContent(); // 返回评论内容
        } else if (targetType.equals("user") && args[0] instanceof User) {
            User user = (User) args[0];
            return user.getUserName(); // 返回用户名
        }
        return "未知目标";
    }

}

