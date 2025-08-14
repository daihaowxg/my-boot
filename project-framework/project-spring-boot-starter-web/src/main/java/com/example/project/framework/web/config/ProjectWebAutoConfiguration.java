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


    /**
     * <p>{@code **.controller.admin.**} 包下有 {@code @RestController} 注解的 {@code Controller}，统一添加 {@code /admin-api} 前缀
     * <p>{@code **.controller.app.**} 包下有 {@code @RestController} 注解的 {@code Controller}，统一添加 {@code /app-api} 前缀
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurePathMatch(configurer, webProperties.getAdminApi());
        configurePathMatch(configurer, webProperties.getAppApi());
    }

    /**
     * 包名符合 {@code packagePattern} 的 RestController 会被添加前缀 {@code prefix}
     *
     * @param configurer 配置
     * @param api        API 配置
     * @see ProjectWebProperties.Api
     */
    private void configurePathMatch(PathMatchConfigurer configurer, ProjectWebProperties.Api api) {
        // 默认 AntPathMatcher 使用 /，所以必须指定 . 才能正确匹配 Java 包名
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        //
        configurer.addPathPrefix(api.getPrefix(), // 要添加的前缀
                clazz -> antPathMatcher.match(api.getPackagePattern(), clazz.getPackage().getName()) // 包名符合 packagePattern 要求
                        && clazz.isAnnotationPresent(RestController.class) // 类上有 @RestController 注解
        );
    }
}
