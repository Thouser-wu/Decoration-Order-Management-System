package com.gzu.decoration.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gzu.decoration.dto.ChangePasswordDTO;
import com.gzu.decoration.dto.LoginDTO;
import com.gzu.decoration.entity.User;
import com.gzu.decoration.exception.BusinessException;
import com.gzu.decoration.mapper.UserMapper;
import com.gzu.decoration.service.AuthService;
import com.gzu.decoration.util.JwtUtil;
import com.gzu.decoration.vo.LoginVO;
import com.gzu.decoration.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());

        // 1. 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            log.warn("用户不存在: {}", loginDTO.getUsername());
            throw new BusinessException(400, "用户名或密码错误");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("密码错误: {}", loginDTO.getUsername());
            throw new BusinessException(400, "用户名或密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            log.warn("用户已被禁用: {}", loginDTO.getUsername());
            throw new BusinessException(403, "账号已被禁用，请联系管理员");
        }

        // 4. 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 5. 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("用户登录成功: {}, ID: {}", loginDTO.getUsername(), user.getId());

        // 6. 返回登录信息
        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .build();
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        log.info("获取用户信息: {}", userId);

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        return UserInfoVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(user.getStatus())
                .createTime(user.getCreateTime())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        log.info("用户修改密码: {}", userId);

        // 1. 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 2. 验证原密码
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "原密码错误");
        }

        // 3. 验证新密码和确认密码是否一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new BusinessException(400, "新密码和确认密码不一致");
        }

        // 4. 验证新密码强度（至少6位）
        if (StrUtil.length(changePasswordDTO.getNewPassword()) < 6) {
            throw new BusinessException(400, "新密码长度不能少于6位");
        }

        // 5. 更新密码
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userMapper.updateById(user);

        log.info("用户修改密码成功: {}", userId);
    }

    @Override
    public void logout(Long userId) {
        log.info("用户退出登录: {}", userId);
        // JWT是无状态的，服务端不需要额外操作
        // 如果需要实现黑名单机制，可以在这里将Token加入Redis黑名单
    }
}
