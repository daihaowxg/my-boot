package com.example.project.framework.apilog.core.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.example.project.framework.apilog.config.ProjectApiLogAutoConfiguration;
import com.example.project.framework.common.util.servlet.ServletUtils;
import com.example.project.framework.common.util.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * API 访问日志 Interceptor
 * <p>
 * 目的：在除了 prod 环境之外的环境中，打印请求日志和响应日志。
 * <p>
 * 请求日志包括请求的 URL、参数等信息。
 * 响应日志包括请求的 URL、耗时等信息。
 *
 * @see com.example.project.framework.apilog.core.filter.ApiAccessLogFilter
 * @see ProjectApiLogAutoConfiguration#addInterceptors(InterceptorRegistry)
 */
@Slf4j
public class ApiAccessLogInterceptor implements HandlerInterceptor {

    public static final String ATTRIBUTE_HANDLER_METHOD = "HANDLER_METHOD";

    private static final String ATTRIBUTE_STOP_WATCH = "ApiAccessLogInterceptor.StopWatch";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录 HandlerMethod，提供给 ApiAccessLogFilter 使用
        // HandlerMethod 是 Spring MVC 中 “将 URL 映射到 Java 方法” 的桥梁，它不仅知道“哪个方法处理请求”，还知道“这个方法需要什么参数、返回什么类型、有哪些注解”，是实现灵活请求处理的核心抽象。
        HandlerMethod handlerMethod = handler instanceof HandlerMethod ? (HandlerMethod) handler : null;
        if (handlerMethod != null) {
            request.setAttribute(ATTRIBUTE_HANDLER_METHOD, handlerMethod);
        }

        // 打印 request 日志
        if (!SpringUtils.isProd()) {
            Map<String, String> queryString = ServletUtils.getParamMap(request);
            String requestBody = ServletUtils.isJsonRequest(request) ? ServletUtils.getBody(request) : null;
            if (CollUtil.isEmpty(queryString) && StrUtil.isEmpty(requestBody)) {
                log.info("[preHandle][开始请求 URL({}) 无参数]", request.getRequestURI());
            } else {
                log.info("[preHandle][开始请求 URL({}) 参数({})]", request.getRequestURI(),
                        StrUtil.blankToDefault(requestBody, queryString.toString())); // 请求体为空时，打印请求参数
            }
        }
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request.setAttribute(ATTRIBUTE_STOP_WATCH, stopWatch);
        // 打印 Controller 路径
        printHandlerMethodPosition(handlerMethod);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 打印处理请求的 HandlerMethod 的类名和方法名
     *
     * @param handlerMethod
     */
    private void printHandlerMethodPosition(HandlerMethod handlerMethod) {
        if (handlerMethod == null) {
            return;
        }

        Method method = handlerMethod.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        try {
            // 获取 method 的行号
            Optional<Integer> lineNumber = getLineNumber(clazz, method);
            if (!lineNumber.isPresent()) {
                return;
            }
            // 打印结果
            System.out.println(StrUtil.format("\tController 方法路径：{}({}.java:{})", clazz.getName(), clazz.getSimpleName(), lineNumber.get()));
        } catch (Throwable ex) {
            // 忽略异常
        }
    }

    /**
     * 获取方法所在的行号
     *
     * @param clazz  类信息
     * @param method 方法信息
     * @return 方法所在的行号，如果找不到则返回空
     */
    private Optional<Integer> getLineNumber(Class<?> clazz, Method method) {
        String path = ResourceUtil.getResource(null, clazz).getPath().replace("/target/classes/", "/src/main/java/") + clazz.getSimpleName() + ".java";
        List<String> clazzContents = FileUtil.readUtf8Lines(path);
        return IntStream.range(0, clazzContents.size())
                .filter(i -> clazzContents.get(i).contains(method.getName() + "(")) // 过滤出那些 包含方法名 + 左括号 ( 的行
                .mapToObj(i -> i + 1) // 将行索引（从 0 开始）转换为 实际行号（从 1 开始）
                .findFirst(); // 找到第一个匹配的行号（Optional<Integer> 类型）
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!SpringUtils.isProd()) {
            StopWatch stopWatch = (StopWatch) request.getAttribute(ATTRIBUTE_STOP_WATCH);
            stopWatch.stop();
            log.info("[afterCompletion][完成请求 URL({}) 耗时({} ms)]",
                    request.getRequestURI(), stopWatch.getTotalTimeMillis());
        }
    }
}
