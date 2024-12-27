package com.example.firenewsbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.User;
import com.example.firenewsbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * 获取特定用户
     * @return User
     */
    @GetMapping("/getUserById")
    public BaseResponse<User> getUserById(Integer id){
        return ResultUtils.success(userService.getUserById(id));
    }

    /**
     * 账号搜索用户
     * @return User
     */
    @GetMapping("/getUserByAccount")
    public BaseResponse<List<User>> getUserByAccount(String userAccount){
        return ResultUtils.success(userService.getUserByAccount(userAccount));
    }

    /**
     * 分页获取用户
     * @return User
     */
    @GetMapping("/getUsersByPage")
    public BaseResponse<Page<User>> getUsersByPage(
            @RequestParam int pageNo,
            @RequestParam int pageSize){
        Page<User> usersPage = userService.getUsersPage(pageNo, pageSize);
        return ResultUtils.success(usersPage);
    }

    /**
     * 获取当前登录用户
     * @return User
     */
    @GetMapping("/getLoginUser")
    public BaseResponse<User> getLoginUser(HttpServletRequest request){
        return ResultUtils.success(userService.getLoginUser(request));
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

    /**
     * 更新用户
     * @return User
     */
    @PostMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (user.getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // 确保 id 是数字类型
        try {
            user.setId(Long.parseLong(user.getId().toString())); // 或者 Integer.parseInt()
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }

        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }


    /**
     * 删除用户
     * @param id
     * @return User
     */
    @PostMapping("/deleteUser")
    public BaseResponse<User> deleteUser(Integer id){
        return ResultUtils.success(userService.deleteUser(id));
    }
}
