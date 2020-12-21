package com.rabbit.system.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class IMailServiceTest {
	@Autowired
	IMailService mailService;

	@Test
	public void testSendSimpleMail() {
		String to = "basicworld@163.com";
		String subject = "嫦娥5号的祝福";
		String content = "你好中国，我是嫦娥5号，时间：" + new Date().toString();

		mailService.sendSimpleMail(to, subject, content);
	}

	@Test
	public void testReloadRedisCacheAndSendSimpleMail() {
		mailService.reloadMailConfigFromCache();
		String to = "basicworld@163.com";
		String subject = "嫦娥5号的祝福reload";
		String content = "你好中国，我是嫦娥5号，时间：" + new Date().toString();

		mailService.sendSimpleMail(to, subject, content);
	}

}
