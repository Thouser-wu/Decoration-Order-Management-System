package com.gzu.decoration.service;

import com.gzu.decoration.dto.ChangePasswordDTO;
import com.gzu.decoration.dto.LoginDTO;
import com.gzu.decoration.vo.LoginVO;
import com.gzu.decoration.vo.UserInfoVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录请求
     * @return 登录响应（包含Token）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 获取当前用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param changePasswordDTO 修改密码请求
     */
    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);

    /**
     * 退出登录
     *
     * @param userId 用户ID
     */
    void logout(Long userId);
}
