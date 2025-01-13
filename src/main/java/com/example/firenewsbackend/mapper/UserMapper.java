package com.example.firenewsbackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.firenewsbackend.model.dto.user.UpdateByUserRequest;
import com.example.firenewsbackend.model.entity.User;
import org.apache.ibatis.annotations.Select;


public interface UserMapper extends BaseMapper<User> {

    @Select(
            "UPDATE user SET " +
                    "userAccount = #{userAccount}, " +
                    "unionId = #{unionId}, " +
                    "mpOpenId = #{mpOpenId}, " +
                    "userName = #{userName}, " +
                    "userAvatar = #{userAvatar}, " +
                    "userProfile = #{userProfile}, " +
                    "email = #{email} " +
                    "WHERE id = #{id}"
    )
    void updateByUser(UpdateByUserRequest user);
}
