package com.lingao.snkcomm.common.exception;

import com.lingao.snkcomm.common.api.IErrorCode;

/**
 * @author lingao.
 * @description 错误异常接口
 * @date 2022/5/2 - 17:10
 */
public class ApiException extends RuntimeException{
    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
