# API 日誌記錄設定說明

## 概述

本專案已設定自動的 API 請求日誌記錄功能，類似於 NestJS 的日誌記錄方式。每次呼叫 API 時，都會在 IDE 的 terminal 中自動顯示請求的開始和結束訊息。

## 功能特點

1. **自動記錄所有 API 請求**：無需在每個 Controller 方法中手動加入日誌
2. **記錄請求詳情**：包含 HTTP 方法、URL、狀態碼、耗時等資訊
3. **區分成功和失敗**：自動識別請求是否成功
4. **簡潔易讀**：日誌格式清晰，便於開發和除錯

## 日誌格式範例

### 成功的 API 請求

```
2024-01-15 10:30:45.123 [http-nio-8081-exec-1] INFO  c.f.c.a.c.LoggingInterceptor - API 請求開始 - POST /api/projects - 時間: 2024-01-15 10:30:45.123
2024-01-15 10:30:45.456 [http-nio-8081-exec-1] INFO  c.f.c.a.c.LoggingInterceptor - API 請求完成 - POST /api/projects - 狀態: 201 (成功) - 耗時: 333ms - 時間: 2024-01-15 10:30:45.456
```

### 失敗的 API 請求

```
2024-01-15 10:31:00.789 [http-nio-8081-exec-2] INFO  c.f.c.a.c.LoggingInterceptor - API 請求開始 - GET /api/projects/999 - 時間: 2024-01-15 10:31:00.789
2024-01-15 10:31:00.890 [http-nio-8081-exec-2] ERROR c.f.c.a.c.LoggingInterceptor - API 請求失敗 - GET /api/projects/999 - 狀態: 404 - 耗時: 101ms - 錯誤: 專案不存在 - 時間: 2024-01-15 10:31:00.890
```

## 設定檔案

### application.properties

```properties
# 日誌設定
logging.level.com.fourseasons.crowdfunding=INFO
logging.level.org.springframework.web=INFO
logging.level.org.springframework.security=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# HTTP 請求日誌設定
logging.level.org.springframework.web.servlet.DispatcherServlet=DEBUG
logging.level.org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping=TRACE
```

## 核心組件

### 1. LoggingInterceptor

- 位置：`src/main/java/com/fourseasons/crowdfunding/app/config/LoggingInterceptor.java`
- 功能：攔截所有 API 請求並記錄日誌
- 記錄內容：請求開始、結束、狀態碼、耗時、錯誤訊息

### 2. WebConfig

- 位置：`src/main/java/com/fourseasons/crowdfunding/app/config/WebConfig.java`
- 功能：註冊日誌攔截器
- 適用範圍：所有 `/api/**` 路徑，排除 Swagger 相關路徑

## 使用方式

1. **啟動應用程式**：正常啟動 Spring Boot 應用程式
2. **呼叫 API**：使用任何 HTTP 客戶端呼叫 API
3. **查看日誌**：在 IDE 的 terminal 中查看自動產生的日誌訊息

## 優勢

1. **零侵入性**：無需修改現有的 Controller 程式碼
2. **自動化**：所有 API 請求都會自動記錄
3. **一致性**：統一的日誌格式和記錄方式
4. **可維護性**：集中管理日誌邏輯，易於維護和修改

## 自訂設定

如需調整日誌格式或記錄內容，可以修改 `LoggingInterceptor` 類別中的相關方法。

## 注意事項

- 日誌記錄會略微影響 API 效能，但影響很小
- 在生產環境中，可以調整日誌級別來控制輸出量
- 敏感資訊（如密碼）不會被記錄在日誌中
