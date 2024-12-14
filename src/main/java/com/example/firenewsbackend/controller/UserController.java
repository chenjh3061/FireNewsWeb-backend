package com.example.firenewsbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取所有用户
     * @return User
     */
    @GetMapping("/getAllUsers")
    public String getUser(){
        return "AllUsers";
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
