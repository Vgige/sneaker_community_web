package com.lingao.snkcomm.service.impl;
import com.lingao.snkcomm.common.exception.ApiAsserts;
import com.lingao.snkcomm.model.dto.EmailDTO;
import com.lingao.snkcomm.service.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author lingao.
 * @description 邮箱发送接口实现类
 * @date 2022/5/10 - 11:00
 */
@Service
@Slf4j
public class EmailServiceImpl implements IEmailService{

    @Value("${spring.mail.sendname}")
    private String sendname;
    @Value("${spring.mail.username}")
    private String username;

    @Resource
    JavaMailSenderImpl javaMailSender;

    @Override
    public void send(EmailDTO emailDTO) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //邮箱发送内容组成
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(emailDTO.getSubject());
            helper.setText(emailDTO.getContent(), true);
            helper.setTo(emailDTO.getTos().get(0));
            helper.setFrom( sendname+"<"+username +">");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            ApiAsserts.fail("邮件发送失败");
        }

    }
}
