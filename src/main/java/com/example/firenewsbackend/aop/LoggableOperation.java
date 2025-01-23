package com.example.firenewsbackend.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableOperation {
    String operationName();  // 操作名称，例如“发布文章”
    String actionType();     // 动作类型，例如“publish”
    String targetType();     // 目标类型，例如“article”
}

