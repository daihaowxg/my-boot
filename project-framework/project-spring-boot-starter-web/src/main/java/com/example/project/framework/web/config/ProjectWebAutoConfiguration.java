package com.example.project.framework.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@AutoConfiguration
@EnableConfigurationProperties(ProjectWebProperties.class)
public class ProjectWebAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private ProjectWebProperties webProperties;

    /**
     * 应用名
     */
    @Value("${spring.application.name}")
    private String applicationName;


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurePathMatch(configurer, webProperties.getAdminApi());
        configurePathMatch(configurer, webProperties.getAppApi());
    }

    /**
     * 设置 API 前缀，仅仅匹配 **.controller.admin.** 或 **.controller.app.** 包下的
     *
     * @param configurer 配置
     * @param api        API 配置
     */
    private void configurePathMatch(PathMatchConfigurer configurer, ProjectWebProperties.Api api) {
        // 默认 AntPathMatcher 使用 /，所以必须指定 . 才能正确匹配 Java 包名
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        configurer.addPathPrefix(api.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class)
                && antPathMatcher.match(api.getController(), clazz.getPackage().getName())); // 仅仅匹配 controller 包
    }
}
