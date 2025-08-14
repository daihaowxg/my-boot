package com.example.project.framework.apilog.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration(after = ProjectApiLogAutoConfiguration.class)
public class ProjectApiLogAutoConfiguration implements WebMvcConfigurer {

    // @Bean
    // @ConditionalOnProperty(prefix = "project.access-log", value = "enable", matchIfMissing = true) // 允许使用 project.access-log.enable=false 禁用访问日志
    // public FilterRegistrationBean<ApiAccessLogFilter>


}
