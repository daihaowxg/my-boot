package com.example.project.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {


    MEMBER(1, "会员"), // 面向 c 端，普通用户
    ADMIN(2, "管理员"); // 面向 b 端，管理后台


    /**
     * 类型
     */
    private final Integer value;

    /**
     * 类型名
     */
    private final String name;
}
