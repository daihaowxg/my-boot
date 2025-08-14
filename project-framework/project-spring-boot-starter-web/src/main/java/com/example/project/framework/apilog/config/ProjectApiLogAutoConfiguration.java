package com.example.project.framework.apilog.config;

import com.example.project.framework.apilog.core.interceptor.ApiAccessLogInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration(after = ProjectApiLogAutoConfiguration.class)
public class ProjectApiLogAutoConfiguration implements WebMvcConfigurer {

    // @Bean
    // @ConditionalOnProperty(prefix = "project.access-log", value = "enable", matchIfMissing = true) // 允许使用 project.access-log.enable=false 禁用访问日志
    // public FilterRegistrationBean<ApiAccessLogFilter>


    /**
     * 添加 API 访问日志拦截器
     * <p>
     * @param registry 拦截器注册器
     * @see com.example.project.framework.apilog.core.interceptor.ApiAccessLogInterceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiAccessLogInterceptor());
    }
}
