package com.rabbit.system.service.impl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.system.constant.ConfigConstants;
import com.rabbit.system.service.IMailService;
import com.rabbit.system.service.ISysConfigService;

@Service
public class MailServiceImpl implements IMailService {
	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	/**
	 * Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
	 */
	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private ISysConfigService configService;

	@Override
	public ValidResult sendSimpleMail(String to, String subject, String content) {
		// 创建SimpleMailMessage对象
		SimpleMailMessage message = new SimpleMailMessage();
		// 邮件发送人
		Object from = configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SMTP_FROM);
		if (StringUtils.isNull(from)) {
			logger.error("缺少必要参数，请检查配置文件或数据库：" + ConfigConstants.KEY_OF_MAIL_SMTP_FROM);
			throw new RuntimeException("缺少必要参数，请检查配置文件或数据库：" + ConfigConstants.KEY_OF_MAIL_SMTP_FROM);
		}
		message.setFrom((String) from);
		// 邮件接收人
		message.setTo(to);
		// 邮件主题
		message.setSubject(subject);
		// 邮件内容
		message.setText(content);

		mailSender.send(message);

		return ValidResult.success();
	}

	@Override
	public Boolean reloadMailConfigFromCache() {
		String host = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SMTP_HOST);
		String port = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SMTP_PORT);
		String username = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SMTP_USERNAME);
		String password = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SMTP_PASSWORD);
		String userSSL = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SMTP_USE_SSL);
		mailSender.setHost(host);
		mailSender.setPort(Integer.parseInt(port));
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		Properties p = new Properties();
		p.setProperty("mail.smtp.auth", "true");
		if ("true".equals(userSSL)) {
			p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		mailSender.setJavaMailProperties(p);
		return true;
	}

}
