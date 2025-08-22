package com.fourseasons.crowdfunding.app.security;

import com.fourseasons.crowdfunding.app.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 認證過濾器
 * 攔截所有請求，驗證 JWT Token 並設置認證資訊
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            // 從請求中提取 JWT Token
            String jwt = parseJwt(request);

            // 如果 Token 存在且有效，則進行認證
            if (jwt != null && jwtUtils.isTokenValid(jwt)) {
                String username = jwtUtils.extractUsername(jwt);

                // 載入使用者詳情
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 驗證 Token 與使用者詳情是否匹配
                if (jwtUtils.validateToken(jwt, userDetails)) {
                    // 創建認證 Token
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    // 設置認證詳情
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 設置認證到 Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    logger.debug("User '{}' authenticated successfully", username);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // 繼續過濾器鏈
        filterChain.doFilter(request, response);
    }

    /**
     * 從 HTTP 請求中解析 JWT Token
     * 
     * @param request HTTP 請求
     * @return JWT Token 或 null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
