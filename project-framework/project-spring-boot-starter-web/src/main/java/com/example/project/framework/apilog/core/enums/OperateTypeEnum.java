package com.example.project.framework.apilog.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志的操作类型
 */
@Getter
@AllArgsConstructor
public enum OperateTypeEnum {

    GET(1, "查询"),
    CREATE(2, "新增"),
    UPDATE(3, "修改"),
    DELETE(4, "删除"),
    IMPORT(5, "导入"),
    EXPORT(6, "导出"),
    UNKNOW(0, "未知");

    private final Integer type;
    private final String description;
}
