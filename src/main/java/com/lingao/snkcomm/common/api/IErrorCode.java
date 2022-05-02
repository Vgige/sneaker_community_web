package com.lingao.snkcomm.common.api;

/**
 * @author lingao.
 * @description 返回编码接口
 * @date 2022/5/2 - 17:06
 */
public interface IErrorCode {
    /**
     * 错误编码: -1失败;200成功
     *
     * @return 错误编码
     */
    Integer getCode();

    /**
     * 错误描述
     *
     * @return 错误描述
     */
    String getMessage();
}
