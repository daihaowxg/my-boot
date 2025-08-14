package com.example.project.framework.apilog.core.filter;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.apilog.core.interceptor.ApiAccessLogInterceptor;
import com.example.project.framework.common.biz.infra.logger.ApiAccessLogCommonApi;
import com.example.project.framework.common.biz.infra.logger.dto.ApiAccessLogCreateReqDTO;
import com.example.project.framework.common.util.servlet.ServletUtils;
import com.example.project.framework.web.core.filter.ApiRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * API 访问日志过滤器
 * <p>
 * 目的：记录 API 访问日志到数据库中
 */
@RequiredArgsConstructor
public class ApiAccessLogFilter extends ApiRequestFilter {

    private final ApiAccessLogCommonApi apiAccessLogApi;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 执行过滤链
            filterChain.doFilter(request, response);
            // 执行正常，记录日志
            createApiAccessLog(request, null);
        } catch (Exception e) {
            // 出现异常，记录日志
            createApiAccessLog(request, e);
            // 记录完日志以后，再重新抛出异常
            throw e;
        }
    }

    private void createApiAccessLog(HttpServletRequest request, Exception e) {
        try {
            // 判断是否启用日志记录
            if (!isApiAccessLogEnabled(request)) {
                return;
            }

            // 设置访问日志的基本信息
            ApiAccessLogCreateReqDTO accessLog = buildApiAccessLog(request, e);

            // 保存访问日志
            apiAccessLogApi.createApiAccessLog(accessLog);
        } catch (Throwable throwable) {

        }
    }

    /**
     * 构建 API 访问日志对象
     *
     * @param request       请求对象
     * @param e             异常信息，如果没有异常则为 null
     * @return ApiAccessLogCreateReqDTO 对象
     */
    private ApiAccessLogCreateReqDTO buildApiAccessLog(HttpServletRequest request, Exception e) {
        ApiAccessLogCreateReqDTO accessLog = new ApiAccessLogCreateReqDTO();
        // 链路追踪编号
        // accessLog.setTraceId();
        // 用户编号
        // accessLog.setUserId();
        // 用户类型
        // accessLog.setUserType();
        // 应用名
        // accessLog.setApplicationName(ServletUtils.getApplicationName(request));
        // 请求方法名
        // 请求地址
        // 请求参数
        Map<String, String> queryString = ServletUtils.getParamMap(request);
        String requestBody = ServletUtils.isJsonRequest(request) ? ServletUtils.getBody(request) : null;

        // 响应体
        // 用户 IP
        // 浏览器 UA
        // 操作模块
        // 操作名
        // 操作类型
        // 开始请求时间
        LocalDateTime beginTime = LocalDateTime.now();

        // 结束请求时间
        // 执行时长
        // 结果码
        // 结果描述


        return null;
    }

    /**
     * 是否开启 API 访问日志
     */
    private boolean isApiAccessLogEnabled(HttpServletRequest request) {
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(ApiAccessLogInterceptor.ATTRIBUTE_HANDLER_METHOD);
        if (handlerMethod == null) {
            return false;
        }
        ApiAccessLog annotation = handlerMethod.getMethodAnnotation(ApiAccessLog.class);
        // 有 @ApiAccessLog 注解，并且注解的 enable 属性为 true，则开启日志记录
        return annotation != null && annotation.enable();
    }
}
