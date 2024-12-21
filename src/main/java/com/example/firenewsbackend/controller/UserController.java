package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.User;
import com.example.firenewsbackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService  userService;

    /**
     * 获取所有用户
     * @return User
     */
    @GetMapping("/getAllUsers")
    public BaseResponse<List<User>> getAllUsers(){
        return ResultUtils.success(userService.getAllUsers());
    }

    /**
     * 获取用户
     * @return User
     */
    @GetMapping("/getUserById")
    public BaseResponse<User> getUserById(Integer id){
        return ResultUtils.success(userService.getUserById(id));
    }


    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     */
    @PostMapping("/register")
    public BaseResponse<User> register(String userAccount, String userPassword, String checkPassword){
        return ResultUtils.success(userService.register(userAccount, userPassword, checkPassword));
    }

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     */
    @PostMapping("/login")
    public BaseResponse<User> login(String userAccount, String userPassword){
        return ResultUtils.success(userService.login(userAccount, userPassword));
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("/getCurrentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        return ResultUtils.success(userService.getLoginUser(request));
    }
}
