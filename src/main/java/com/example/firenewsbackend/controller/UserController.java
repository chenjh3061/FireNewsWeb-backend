package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.entity.User;
import com.example.firenewsbackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public String getUserById(){
        return "";
    }

}
