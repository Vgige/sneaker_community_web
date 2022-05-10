package com.lingao.snkcomm;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class SneakerCommunityWebApplicationTests {
    @Autowired
    JavaMailSenderImpl mailSender;
    @Test
    void contextLoads() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setSubject("spring-emailSend");
        mimeMessageHelper.setText("这是一封复杂的邮件");
        // mimeMessageHelper.setText("<p style="color:red">这是一封复杂的邮件</p>",true);
        mimeMessageHelper.setFrom("1634202401@qq.com");
        mimeMessageHelper.setTo("1634202401@qq.com");
        mimeMessageHelper.addAttachment("1.jpg",new File("C:\\Users\\DELL\\Desktop\\1.jpg"));
        mailSender.send(mimeMessage);
    }

}
