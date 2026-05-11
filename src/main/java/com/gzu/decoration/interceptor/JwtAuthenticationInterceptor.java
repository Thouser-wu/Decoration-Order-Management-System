package com.gzu.decoration.interceptor;

import com.gzu.decoration.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * 拦截请求并验证 Token 的有效性
 * 
 * @author Decoration Order System
 * @since 2024-01-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     * 
     * @param request  当前请求
     * @param response 当前响应
     * @param handler  当前处理器
     * @return 是否继续执行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // 获取请求头中的 Token
        String token = request.getHeader("Authorization");

        // 如果 Token 为空，直接返回 401
        if (token == null || token.isEmpty()) {
            log.warn("请求未携带 Token，URL: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\",\"data\":null}");
            return false;
        }

        // 去除 "Bearer " 前缀（如果有的话）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 Token
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token 无效或已过期，URL: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期，请重新登录\",\"data\":null}");
            return false;
        }

        // Token 验证通过，将用户信息存入请求属性中，方便后续使用
        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);

        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        request.setAttribute("role", role);

        log.debug("Token 验证通过，用户: {}, URL: {}", username, request.getRequestURI());
        
        return true;
    }
}
