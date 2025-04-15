package com.example.firenewsbackend.aop;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.mapper.LogMapper;
import com.example.firenewsbackend.model.entity.Article;
import com.example.firenewsbackend.model.entity.Comments;
import com.example.firenewsbackend.model.entity.Log;
import com.example.firenewsbackend.model.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * 精确获取目标ID
     */
    private Long getTargetId(String targetType, Object[] args) {
        try {
            // 1. 首先尝试从方法参数中直接获取ID
            for (Object arg : args) {
                if (arg == null) continue;

                // 处理实体对象
                if (targetType.equalsIgnoreCase("article") && arg instanceof Article) {
                    return ((Article) arg).getId();
                }
                else if (targetType.equalsIgnoreCase("comment") && arg instanceof Comments) {
                    return ((Comments) arg).getId();
                }
                else if (targetType.equalsIgnoreCase("user") && arg instanceof User) {
                    return ((User) arg).getId();
                }
                // 处理直接传递的ID参数
                else if (arg instanceof Long) {
                    return (Long) arg;
                }
                else if (arg instanceof String) {
                    try {
                        return Long.parseLong((String) arg);
                    } catch (NumberFormatException ignored) {}
                }
            }

            // 2. 尝试从请求参数中获取ID
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            // 检查路径变量 (如 /articles/{id})
            String pathInfo = request.getRequestURI();
            Matcher matcher = Pattern.compile(".*/(\\d+)$").matcher(pathInfo);
            if (matcher.find()) {
                return Long.parseLong(matcher.group(1));
            }

            // 检查查询参数 (如 ?id=123)
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    return Long.parseLong(idParam);
                } catch (NumberFormatException e) {
                    log.warn("无法解析ID参数: {}", idParam);
                }
            }

            // 3. 最后尝试从请求体中提取ID（针对POST/PATCH请求）
            if ("POST".equalsIgnoreCase(request.getMethod())
                    || "PATCH".equalsIgnoreCase(request.getMethod())) {
                try (InputStream inputStream = request.getInputStream()) {
                    String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                    if (body.contains("\"id\":")) {
                        // 简单提取ID - 仅适用于简单JSON
                        String idStr = body.split("\"id\":")[1].split("[,\\}]")[0].trim();
                        return Long.parseLong(idStr.replaceAll("\"", ""));
                    }
                } catch (Exception e) {
                    log.warn("解析请求体获取ID失败", e);
                }
            }

            log.warn("无法确定目标ID，目标类型: {}", targetType);
            return null;
        } catch (Exception e) {
            log.error("获取目标ID异常", e);
            return null;
        }
    }

    /**
     * 获取目标信息（如文章标题、评论内容等）
     */
    private String getTargetInfo(String targetType, Object[] args) {
        try {
            // 遍历所有参数寻找匹配的目标对象
            for (Object arg : args) {
                if (arg == null) continue;

                if (targetType.equalsIgnoreCase("article") && arg instanceof Article) {
                    Article article = (Article) arg;
                    return article.getArticleTitle() != null ?
                            article.getArticleTitle() :
                            "文章ID: " + article.getId();
                }
                else if (targetType.equalsIgnoreCase("comment") && arg instanceof Comments) {
                    Comments comment = (Comments) arg;
                    return comment.getContent() != null ?
                            comment.getContent() :
                            "评论ID: " + comment.getId();
                }
                else if (targetType.equalsIgnoreCase("user") && arg instanceof User) {
                    User user = (User) arg;
                    return user.getUserName() != null ?
                            user.getUserName() :
                            "用户ID: " + user.getId();
                }
            }

            // 如果没有匹配到特定对象，尝试获取ID参数
            for (Object arg : args) {
                if (arg instanceof Long) {
                    return "ID: " + arg;
                }
            }

            return "目标类型: " + targetType;
        } catch (Exception e) {
            log.error("获取目标信息失败", e);
            return "解析失败";
        }
    }

}

