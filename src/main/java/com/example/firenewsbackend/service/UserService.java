package com.example.firenewsbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.constant.UserConstant;
import com.example.firenewsbackend.exception.BusinessException;
import com.example.firenewsbackend.model.dto.user.UpdateByUserRequest;
import com.example.firenewsbackend.model.entity.User;
import com.example.firenewsbackend.mapper.UserMapper;
import com.example.firenewsbackend.model.vo.LoginUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.firenewsbackend.constant.UserConstant.USER_LOGIN_STATE;

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
        StpUtil.checkLogin();
        String userRole = (String) StpUtil.getTokenSession().get("UserRole");
        System.out.println(userRole+ " 用户角色"+ UserConstant.ADMIN_ROLE);
        // 如果是管理员角色
        if (!userRole.equals(UserConstant.ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);  // 权限不足
        }
        return userMapper.selectList(null);
    }

    /**
     * 获取用户
     * @return User
     */
    public User getUserById(Long id) {
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不能为空");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "两次输入的密码不一致");
        }

        // 2. 判断账户是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "该账户已存在");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(userPassword); // 加密密码

        // 3. 创建用户并保存到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword); // 注意：生产环境下需对密码进行加密处理
        user.setUserRole("user");
        userMapper.insert(user);

        return user;
    }

    /**
     * 新增用户
     */
    public void addUser(User user) {
        // 1. 参数校验
        if (user.getUserAccount() == null || user.getUserPassword() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 2. 判断账户是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", user.getUserAccount());
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new IllegalArgumentException("该账户已存在");
        }

        // 3. 创建用户并保存到数据库
        user.setUserRole("user");
        userMapper.insert(user);
    }


    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     */
    public LoginUserVO login(String userAccount, String userPassword) {
        // 非空校验
        if (userAccount == null || userPassword == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 查询用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User user = userMapper.selectOne(queryWrapper);

        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户不存在");
        }

        // 判断用户是否被禁用
        if (UserConstant.BAN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户已被禁用");
        }

        // 判断密码是否正确
        if (!userPassword.equals(user.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "密码错误");
        }

        // 生成Token并保存
        StpUtil.login(user.getId());
        StpUtil.getTokenSession().set("UserRole", user.getUserRole());
        StpUtil.getTokenSession().set("UserAccount", user.getUserAccount());
        String tokenValue = StpUtil.getTokenValue();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(userPassword); // 加密密码

        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        loginUserVO.setToken(tokenValue);

        // 返回用户信息
        return loginUserVO;
    }

    /**
     * 获取当前登录用户
     */
    public User getLoginUser() {
        Long userId =  StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        String role = (String) StpUtil.getTokenSession().get("UserRole");
        user.setUserRole(role);
        return user;
    }
    // 加上Token
    public LoginUserVO getLoginUserVO(User user,String tokenValue){
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        loginUserVO.setToken(tokenValue);
        return loginUserVO;
    }


    /**
     * 更新用户
     */
    public void updateUser(User user){
        userMapper.updateById(user);
    }

    public void updateUserByUser(UpdateByUserRequest user){
        userMapper.updateByUser(user);
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

    /**
     * 用户登出
     */
    public boolean userLogout() {
        if ( !StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        Object userId = userMapper.selectById(StpUtil.getLoginIdAsLong()).getId();
        System.out.println("用戶id：" + userId);
        // 移除登录态
        StpUtil.logout(StpUtil.getLoginId(userId));
        return true;
    }
}
