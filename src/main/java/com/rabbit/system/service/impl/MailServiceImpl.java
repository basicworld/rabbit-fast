package com.rabbit.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.system.service.IMailService;

@Service
public class MailServiceImpl implements IMailService {
	/**
	 * Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
	 */
	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.from}")
	private String from;

	@Override
	public ValidResult sendSimpleMail(String to, String subject, String content) {
		// 创建SimpleMailMessage对象
		SimpleMailMessage message = new SimpleMailMessage();
		// 邮件发送人
		message.setFrom(from);
		// 邮件接收人
		message.setTo(to);
		// 邮件主题
		message.setSubject(subject);
		// 邮件内容
		message.setText(content);

		mailSender.send(message);

		return ValidResult.success();
	}

}
