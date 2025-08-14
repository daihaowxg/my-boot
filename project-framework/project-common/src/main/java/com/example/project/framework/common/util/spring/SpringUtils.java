package com.example.project.framework.common.util.spring;

import cn.hutool.extra.spring.SpringUtil;

/**
 * Spring 工具类，继承自 Hutool 的 SpringUtil，提供对 Spring 应用上下文的便捷访问。
 */
public class SpringUtils extends SpringUtil {

    /**
     * 判断当前环境是否为生产环境。
     *
     * @return 如果当前激活的 Spring 配置文件为 "prod"，则返回 true；否则返回 false。
     */
    public static boolean isProd() {
        String activeProfile = getActiveProfile();
        return "prod".equals(activeProfile);
    }
}
