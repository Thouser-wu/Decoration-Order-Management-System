package com.gzu.decoration.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gzu.decoration.entity.User;
import com.gzu.decoration.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化配置
 * 应用启动时自动执行
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已存在 admin 用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, "admin");
        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser == null) {
            // 创建默认管理员账号
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("123456"));
            adminUser.setRealName("系统管理员");
            adminUser.setPhone("13800138000");
            adminUser.setEmail("admin@example.com");
            adminUser.setRole("admin");
            adminUser.setStatus(1);
            
            userMapper.insert(adminUser);
            
            log.info("========================================");
            log.info("默认管理员账号已创建:");
            log.info("用户名: admin");
            log.info("密码: 123456");
            log.info("请及时修改默认密码！");
            log.info("========================================");
        } else {
            log.info("管理员账号已存在，跳过初始化");
        }
    }
}
