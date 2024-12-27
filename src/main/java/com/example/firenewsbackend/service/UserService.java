package com.example.firenewsbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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


    /**
     * 更新用户
     * @return User
     */
    public User updateUser(User user){
        userMapper.updateById(user);
        return user;
    }

    /**
     * 删除用户
     * @param id
     * @return User
     */
    public User deleteUser(Integer id){
        // 假设 User 表有一个名为 isDelete 的字段，值为 1 表示删除
        User user = userMapper.selectById(id); // 获取用户信息
        if (user != null) {
            user.setIsDelete(1); // 设置 isDelete 字段为 1，表示已删除
            userMapper.updateById(user); // 执行更新
        }
        return user;
    }

    /**
     * 搜索用户
     * @return User
     */
    public List<User> getUserByAccount(String userAccount) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("userAccount", userAccount);
        return userMapper.selectList(queryWrapper);
    }

    public Page<User> getUsersPage(int pageNo, int pageSize) {
        Page<User> page = new Page<>(pageNo, pageSize);
        return userMapper.selectPage(page, null);
    }
}
