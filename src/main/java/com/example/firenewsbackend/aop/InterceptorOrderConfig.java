package com.example.firenewsbackend.aop;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; /**
 * 请求响应日志 AOP
 **/

@Configuration
public class InterceptorOrderConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 先添加 Sa-Token 拦截器，优先级更高
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**").order(0);
        // 后添加日志切面对应的拦截器（LogInterceptor 通过 @Aspect 生效，需调整其 Order）
    }
}
