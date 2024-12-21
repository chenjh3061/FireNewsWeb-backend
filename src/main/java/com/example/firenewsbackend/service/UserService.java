package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.firenewsbackend.model.entity.User;
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
     * 获取用户
     * @return User
     */
    public User getUserById(Integer id) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
    }

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 注册的用户信息或 null（注册失败）
     */
    public User register(String userAccount, String userPassword, String checkPassword) {
        // 1. 参数校验
        if (userAccount == null || userPassword == null || checkPassword == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }

        // 2. 判断账户是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new IllegalArgumentException("该账户已存在");
        }

        // 3. 创建用户并保存到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword); // 注意：生产环境下需对密码进行加密处理
        user.setUserRole("user");
        userMapper.insert(user);

        return user;
    }


    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     */
    public User login(String userAccount, String userPassword) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("userAccount", userAccount).eq("userPassword", userPassword));
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    public User getLoginUser(javax.servlet.http.HttpServletRequest request) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("userAccount", request.getHeader("userAccount")));
    }

}
