package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Select("SELECT * FROM notification WHERE userId = #{userId} AND status = 1 AND (startTime IS NULL OR startTime <= NOW()) AND (endTime IS NULL OR endTime >= NOW())")
    List<Notion> getActiveNotifications(@Param("userId") String userId);

    @Update("UPDATE notification SET status = 0 WHERE id = #{id}")
    void markAsRead(@Param("id") Integer id);
}
