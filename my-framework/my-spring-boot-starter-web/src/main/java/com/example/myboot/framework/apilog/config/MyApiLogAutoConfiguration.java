package com.example.myboot.framework.apilog.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration(after = MyApiLogAutoConfiguration.class)
public class MyApiLogAutoConfiguration implements WebMvcConfigurer {

    // @Bean
    // @ConditionalOnProperty(prefix = "my.access-log", value = "enable", matchIfMissing = true) // 允许使用 my.access-log.enable=false 禁用访问日志
    // public FilterRegistrationBean<ApiAccessLogFilter>


}
