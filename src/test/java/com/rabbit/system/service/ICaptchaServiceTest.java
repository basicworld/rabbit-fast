package com.rabbit.system.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.Captcha;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ICaptchaServiceTest {

	@Autowired
	ICaptchaService capService;

	@Test
	public void validate() {
		try {
			Captcha cap = capService.create();
			TestCase.assertTrue(capService.validate(cap));
		} catch (IOException e) {
		}
	}

}
