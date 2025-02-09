package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.model.entity.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LogMapper {

    @Insert("INSERT INTO log (name, createTime, userAccount, actionType, targetType, targetId, requestMethod, requestPath, requestIP, params) " +
            "VALUES (#{name}, #{createTime}, #{userAccount}, #{actionType}, #{targetType}, #{targetId}, #{requestMethod}, #{requestPath}, #{requestIP}, #{params})")
    int insert(Log dynamicLog);

    @Select("SELECT * FROM log ORDER BY createTime DESC")
    Page<Log> findRecentLogs(Page<Log> page);

}
