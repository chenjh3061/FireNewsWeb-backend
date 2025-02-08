package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.model.entity.Notion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface NotionMapper extends BaseMapper<Notion> {

    @Update("UPDATE notification " +
            "SET title = #{title}, " +
            "content = #{content}, " +
            "startTime = #{startTime}, " +
            "endTime = #{endTime}, " +
            "userId = #{userId}, " +
            "status = #{status}, " +
            "isDelete = #{isDelete} " +
            "WHERE id = #{id}")
    void updateNotion(Notion notion);

    @Select("SELECT * FROM notification WHERE  status = 1 AND (startTime IS NULL OR startTime <= NOW()) AND (endTime IS NULL OR endTime >= NOW())")
    List<Notion> getAllActiveNotifications();

    @Update("UPDATE notification SET status = 0 WHERE id = #{id}")
    Notion markAsRead(@Param("id") Integer id);
}
