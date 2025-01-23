package com.example.firenewsbackend.mapper;

import com.example.firenewsbackend.model.entity.Log;
import org.apache.ibatis.annotations.Insert;

public interface LogMapper {

    @Insert("INSERT INTO log(name, userAccount, createTime, actionType, targetType, targetId, ip) " +
            "VALUES (#{name}, #{userAccount}, #{createTime}, #{actionType}, #{targetType}, #{targetId}, #{ip})")
    int insert(Log dynamicLog);

}
