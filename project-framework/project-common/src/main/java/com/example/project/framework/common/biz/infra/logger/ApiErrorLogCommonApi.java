package com.example.project.framework.common.biz.infra.logger;

import com.example.project.framework.common.biz.infra.logger.dto.ApiErrorLogCreateReqDTO;
import org.springframework.scheduling.annotation.Async;

import javax.validation.Valid;

/**
 * API 错误日志的公共 API 接口
 */
public interface ApiErrorLogCommonApi {
    /**
     * 创建 API 错误日志
     *
     * @param createDTO 创建信息
     */
    void createApiErrorLog(@Valid ApiErrorLogCreateReqDTO createDTO);

    /**
     * 【异步】创建 API 错误日志
     *
     * @param createDTO 错误日志 DTO
     */
    @Async
    default void createApiErrorLogAsync(ApiErrorLogCreateReqDTO createDTO) {
        // 默认实现，子类可以覆盖
        createApiErrorLog(createDTO);
    }
}
