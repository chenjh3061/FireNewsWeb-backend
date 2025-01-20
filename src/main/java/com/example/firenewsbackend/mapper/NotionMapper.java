package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

public interface NotionMapper extends BaseMapper<Notion> {

    @Insert("INSERT INTO notification" +
            " (title, content, startTime, endTime, userId, status, domain, createTime, updateTime, isDelete) " +
            "VALUES (#{title}  , #{content}, #{startTime}, #{endTime}, #{userId}, #{status}, #{domain}, " +
            "#{createTime}, " + "#{updateTime}, #{isDelete})")
    Notion addNotion(Notion notion);

    @Update("UPDATE notification " +
            "SET title = #{title}, " +
            "content = #{content}, " +
            "startTime = #{startTime}, " +
            "endTime = #{endTime}, " +
            "userId = #{userId}, " +
            "status = #{status}, " +
            "domain = #{domain}, " +
            "updateTime = #{updateTime}, " +
            "isDelete = #{isDelete} " +
            "WHERE id = #{id}")
    Notion updateNotion(Notion notion);
}
