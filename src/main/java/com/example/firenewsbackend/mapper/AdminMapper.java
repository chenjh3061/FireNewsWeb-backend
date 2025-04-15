package com.example.firenewsbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.model.dto.AdminStatusDTO;
import com.example.firenewsbackend.model.entity.Article;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper extends BaseMapper<AdminStatusDTO>{

    @Select("update user set password = #{password} where id = #{userId}")
    void resetPassword(String userId, String password);
}
