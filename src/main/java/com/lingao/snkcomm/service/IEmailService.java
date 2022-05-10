package com.lingao.snkcomm.service;

import com.lingao.snkcomm.model.dto.EmailDTO;

/**
 * @author lingao.
 * @description 邮箱服务接口
 * @date 2022/5/10 - 10:59
 */
public interface IEmailService {
    /**
     * 发送邮件
     * @param emailDTO 邮箱列表
     */
    void send(EmailDTO emailDTO);
}
