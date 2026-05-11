package com.gzu.decoration.controller;

import com.gzu.decoration.common.Result;
import com.gzu.decoration.dto.ChangePasswordDTO;
import com.gzu.decoration.dto.LoginDTO;
import com.gzu.decoration.service.AuthService;
import com.gzu.decoration.vo.LoginVO;
import com.gzu.decoration.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "登录、获取用户信息、修改密码等接口")
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录系统")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            LoginVO loginVO = authService.login(loginDTO);
            return Result.success(loginVO);
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.unauthorized("未登录");
            }
            
            UserInfoVO userInfo = authService.getUserInfo(userId);
            return Result.success(userInfo);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, 
                                        HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.unauthorized("未登录");
            }
            
            authService.changePassword(userId, changePasswordDTO);
            return Result.success();
        } catch (Exception e) {
            log.error("修改密码失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "退出当前登录")
    public Result<Void> logout(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId != null) {
                authService.logout(userId);
            }
            return Result.success();
        } catch (Exception e) {
            log.error("退出登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
