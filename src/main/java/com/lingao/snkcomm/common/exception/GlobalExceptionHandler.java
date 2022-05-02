package com.lingao.snkcomm.common.exception;

import com.lingao.snkcomm.common.api.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author lingao.
 * @description 全局异常捕捉
 * @date 2022/5/2 - 17:13
 */
public class GlobalExceptionHandler {
    /**
     * 捕获自定义异常
     */
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public ApiResult<Map<String, Object>> handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return ApiResult.failed(e.getErrorCode());
        }
        return ApiResult.failed(e.getMessage());
    }
}
