/**
 * 描述 HTTP 请求在 Spring Web 应用中的完整处理流程，涵盖 Servlet 层过滤器（Filter）
 * 与 Spring MVC 拦截器（Interceptor）的执行顺序。
 *
 * <p>请求的执行顺序如下：
 * <pre>
 * 1. {@link javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 *    → 可修改 request/response，用于日志、字符编码、CORS、鉴权等预处理
 *
 * 2. 请求进入 {@link org.springframework.web.servlet.DispatcherServlet}
 *
 * 3. {@link org.springframework.web.servlet.HandlerInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)}
 *    → Spring 拦截器前置处理：如登录检查、权限校验、请求上下文构建
 *
 * 4. 执行 {@link org.springframework.web.bind.annotation.RestController} 或 {@link org.springframework.stereotype.Controller} 方法
 *
 * 5. {@link org.springframework.web.servlet.HandlerInterceptor#postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView)}
 *    → 视图渲染前的后置处理：如填充公共模型数据、日志记录
 *
 * 6. 视图渲染（如 JSP、Thymeleaf）或返回 JSON 响应（如 @ResponseBody）
 *
 * 7. {@link org.springframework.web.servlet.HandlerInterceptor#afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception)}
 *    → 请求完成后的资源清理、异常处理、性能统计（无论成功或异常都会执行）
 *
 * 8. Filter 中 {@code chain.doFilter()} 后的代码或 {@code finally} 块
 *    → 响应处理、性能监控收尾、资源释放等
 * </pre>
 *
 * <h3>关键说明：</h3>
 * <ul>
 *   <li><strong>Filter 属于 Servlet 容器层</strong>，由 Web 容器管理，优先执行，能拦截所有请求（包括静态资源）。</li>
 *   <li><strong>Interceptor 属于 Spring 框架层</strong>，仅对 Spring MVC 映射的请求生效。</li>
 *   <li>若 Filter 中未调用 {@code chain.doFilter(request, response)}，请求将被中断，不会进入后续流程。</li>
 *   <li>推荐：Filter 用于通用基础设施（如编码、CORS），Interceptor 用于业务级拦截（如权限、日志上下文）。</li>
 * </ul>
 */
package com.example.project.framework.apilog.core.interceptor;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;