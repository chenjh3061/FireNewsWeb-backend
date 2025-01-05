package com.example.firenewsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.constant.UserConstant;
import com.example.firenewsbackend.exception.BusinessException;
import com.example.firenewsbackend.model.entity.User;
import com.example.firenewsbackend.model.vo.LoginUserVO;
import com.example.firenewsbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

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
    public BaseResponse<User> getUserById(@RequestParam Long id){
        return ResultUtils.success(userService.getUserById(id));
    }

    /**
     * 账号搜索用户
     * @return User
     */
    @GetMapping("/getUserByAccount")
    public BaseResponse<List<User>> getUserByAccount(@RequestParam String userAccount){
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
    public BaseResponse<LoginUserVO> getLoginUser(){
        User user =  userService.getLoginUser();
        String tokenValue = StpUtil.getTokenValue();
        return ResultUtils.success(userService.getLoginUserVO(user,tokenValue));
    }

    /**
     * 用户注册
     * @param params 用户密码
     */
    @PostMapping("/register")
    public BaseResponse<User> register(@RequestBody Map<String, String> params){
        String userAccount = params.get("userAccount");
        String password = params.get("password");
        String checkPassword = params.get("checkPassword");

        if (userAccount == null || password == null || checkPassword == null) {
            return (BaseResponse<User>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        return ResultUtils.success(userService.register(userAccount, password, checkPassword));
    }

    /**
     * 新增用户
     */
//    @PostMapping("/addUser")
//    public BaseResponse<User> addUser(@RequestBody User user) {
//        userService.addUser(user);
//        return ResultUtils.success(user);
//    }

    /**
     * 用户登录
     * @param params 用户账户
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody Map<String, String> params) {
        String userAccount = params.get("userAccount");
        String password = params.get("password");

        if (userAccount == null || password == null) {
            return (BaseResponse<LoginUserVO>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        return ResultUtils.success(userService.login(userAccount, password));
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        boolean result = userService.userLogout();
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/getCurrentUser")
    public BaseResponse<User> getCurrentUser(){
        return ResultUtils.success(userService.getLoginUser());
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
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<User> deleteUser(@RequestParam Integer id){
        return ResultUtils.success(userService.deleteUser(id));
    }


}
