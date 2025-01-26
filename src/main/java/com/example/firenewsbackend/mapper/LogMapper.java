package com.example.firenewsbackend.mapper;

import com.example.firenewsbackend.model.entity.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LogMapper {

    @Insert("INSERT INTO log(name, userAccount, createTime, actionType, targetType, targetId, ip) " +
            "VALUES (#{name}, #{userAccount}, #{createTime}, #{actionType}, #{targetType}, #{targetId}, #{ip})")
    int insert(Log dynamicLog);

    @Select("SELECT * FROM log ORDER BY createTime DESC LIMIT #{limit}")
    List<Log> findRecentLogs(@Param("limit") int limit);
}
