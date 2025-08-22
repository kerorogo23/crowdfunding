package com.fourseasons.crowdfunding.app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * API 請求日誌攔截器
 * 自動記錄所有 API 請求的開始和結束
 */
@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 將開始時間存入請求屬性中，以便在後續處理中使用
        request.setAttribute("startTime", System.currentTimeMillis());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 這裡可以記錄處理過程中的資訊
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString != null ? uri + "?" + queryString : uri;

        int status = response.getStatus();
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;

        if (ex != null) {
            log.error("API 請求失敗 - {} {} - 狀態: {} - 耗時: {}ms - 錯誤: {} - 時間: {}",
                    method, fullUrl, status, duration, ex.getMessage(), LocalDateTime.now().format(formatter));
        } else {
            String statusText = status >= 200 && status < 300 ? "成功" : "失敗";
            log.info("API 請求完成 - {} {} - 狀態: {} ({}) - 耗時: {}ms - 時間: {}",
                    method, fullUrl, status, statusText, duration, LocalDateTime.now().format(formatter));
        }
    }
}
