package com.example.firenewsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.firenewsbackend.aop.LoggableOperation;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.constant.UserConstant;
import com.example.firenewsbackend.exception.BusinessException;
import com.example.firenewsbackend.model.dto.user.UpdateByUserRequest;
import com.example.firenewsbackend.model.entity.User;
import com.example.firenewsbackend.model.vo.LoginUserVO;
import com.example.firenewsbackend.service.UserService;
import com.example.firenewsbackend.utils.RSAUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
    @LoggableOperation(operationName = "用户注册", actionType = "register", targetType = "user")
    @PostMapping("/register")
    public BaseResponse<User> register(@RequestBody Map<String, String> params) {
        String userAccount = params.get("userAccount");
        String password = params.get("password");
        String checkPassword = params.get("checkPassword");

        if (userAccount == null || password == null || checkPassword == null) {
            return (BaseResponse<User>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        try {
            password = RSAUtils.decrypt(password);
            checkPassword = RSAUtils.decrypt(checkPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return (BaseResponse<User>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "密码解密失败");
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
     * 获取公钥
     */
    @GetMapping("/getPublicKey")
    public BaseResponse<String> getPublicKey() {
        return ResultUtils.success(RSAUtils.AES_KEY);
    }


    /**
     * 用户登录
     * @param params 用户账户
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody java.util.Map<String, String> params) throws Exception {
        String userAccount = params.get("userAccount");
        String password = params.get("password");

        if (userAccount == null || password == null) {
            return (BaseResponse<LoginUserVO>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        password = RSAUtils.decrypt(password);
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//            return (BaseResponse<LoginUserVO>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "密码解密失败");
//        }

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
    @LoggableOperation(operationName = "更新用户信息", actionType = "edit", targetType = "user")
    @PostMapping("/updateUserByUser")
    public BaseResponse<UpdateByUserRequest> updateUserByUser(@RequestBody UpdateByUserRequest user) {
        if (user.getId() == null || user.getId() != StpUtil.getLoginIdAsLong()) {
            return (BaseResponse<UpdateByUserRequest>) ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户id错误");
        }

        // 确保 id 是数字类型
        try {
            user.setId(Long.parseLong(user.getId().toString())); // 或者 Integer.parseInt()
        } catch (NumberFormatException e) {
            return (BaseResponse<UpdateByUserRequest>) ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }

        userService.updateUserByUser(user);
        return ResultUtils.success(user);
    }

    /**
     * 用户修改密码
     */
    @LoggableOperation(operationName = "修改密码", actionType = "edit", targetType = "user")
    @PostMapping("/updatePassword")
    public BaseResponse<Boolean> updatePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        String checkPassword = params.get("checkPassword");

        if (oldPassword == null || newPassword == null || checkPassword == null) {
            return (BaseResponse<Boolean>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 获取当前登录用户
        User user = userService.getLoginUser();

        // 判断旧密码是否正确
        if (!oldPassword.equals(user.getUserPassword())) {
            return (BaseResponse<Boolean>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "旧密码错误");
        }

        // 判断新密码是否与确认密码一致
        if (!newPassword.equals(checkPassword)) {
            return (BaseResponse<Boolean>) ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "两次输入的密码不一致");
        }

        // 更新密码
        user.setUserPassword(newPassword);
        userService.updateUser(user);

        return ResultUtils.success(true);
    }

    /**
     * 更新用户（管理员）
     * 涉及敏感信息
     */
    @LoggableOperation(operationName = "更新用户信息", actionType = "edit", targetType = "user")
    @PostMapping("/updateUserByAdmin")
    public BaseResponse<User> updateUserByAdmin(@RequestBody User user) {
        StpUtil.checkRole(UserConstant.ADMIN_ROLE);
        if (user.getId() == null) {
            return (BaseResponse<User>) ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }

        // 确保 id 是数字类型
        try {
            user.setId(Long.parseLong(user.getId().toString())); // 或者 Integer.parseInt()
        } catch (NumberFormatException e) {
            return (BaseResponse<User>) ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }

        userService.updateUser(user);
        return ResultUtils.success(user);
    }

    /**
     * 删除用户
     * @param id
     * @return User
     */
    @LoggableOperation(operationName = "删除用户", actionType = "delete", targetType = "user")
    @PostMapping("/deleteUser")
    public BaseResponse<User> deleteUser(@RequestParam Integer id){
        StpUtil.checkRole(UserConstant.ADMIN_ROLE);
        return ResultUtils.success(userService.deleteUser(id));
    }


}
