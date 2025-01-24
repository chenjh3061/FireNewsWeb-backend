package com.example.firenewsbackend.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.mapper.LogMapper;
import com.example.firenewsbackend.model.entity.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

/**
 * 请求响应日志 AOP
 **/
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    @Resource
    private LogMapper logMapper;

    /**
     * 执行拦截
     */
    @Around("execution(* com.example.firenewsbackend.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";

        // 输出请求日志
        log.info("request start，id: {}, path: {}, ip: {}, params: {}", requestId, url,
                httpServletRequest.getRemoteHost(), reqParam);
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);
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
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取用户信息
        String userAccount = StpUtil.isLogin() ? StpUtil.getTokenSession().get("UserAccount").toString() : "未登录";

        Object[] args = point.getArgs();

        // 记录日志
        Log logRecord = new Log();
        logRecord.setName(operationName + Arrays.toString(args));
        logRecord.setUserAccount(userAccount);
        logRecord.setCreateTime(String.valueOf(LocalDateTime.now()));
        logRecord.setActionType(actionType);
        logRecord.setTargetType(targetType);
        logRecord.setTargetId(getTargetId(targetType, args));

        logMapper.insert(logRecord);

        Object result = point.proceed();

        stopWatch.stop();
        log.info("Annotated request completed, operation: {}, duration: {}ms", operationName, stopWatch.getTotalTimeMillis());
        return result;
    }

    /**
     * 获取目标ID
     */
    private String getTargetId(String url, Object[] args) {
        System.out.println(Arrays.toString(args));
        // 通过URL和参数来获取目标ID（如文章ID、评论ID等）
        if (url.contains("/addArticle")) {
            return (String) args[0];  // 假设文章对象在参数中，第一个参数是文章ID
        } else if (url.contains("/comment")) {
            return (String) args[1];  // 假设评论的文章ID是第二个参数
        }else if (url.contains("/user")) {
            return (String) args[0];
        }
        return null;
    }

}

