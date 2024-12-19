package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.firenewsbackend.entity.User;
import com.example.firenewsbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户服务
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 获取所有用户
     * @return User
     */
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     */
    //long register(String userAccount, String userPassword, String checkPassword);


}
