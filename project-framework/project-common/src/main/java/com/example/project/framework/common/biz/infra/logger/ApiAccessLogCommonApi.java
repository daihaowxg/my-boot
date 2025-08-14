package com.example.project.framework.common.biz.infra.logger;

import com.example.project.framework.common.biz.infra.logger.dto.ApiAccessLogCreateReqDTO;

/**
 * API 访问日志的公共 API 接口
 */
public interface ApiAccessLogCommonApi {

    /**
     * 创建 API 访问日志
     *
     * @param createDTO 日志信息
     */
    void createApiAccessLog(ApiAccessLogCreateReqDTO createDTO);

    /**
     * 【异步】创建 API 访问日志
     *
     * @param createDTO 日志信息
     */
    default void createApiAccessLogAsync(ApiAccessLogCreateReqDTO createDTO) {
        // 默认实现，子类可以覆盖
        createApiAccessLog(createDTO);
    }
}
