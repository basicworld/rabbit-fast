package com.rabbit.system.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.domain.Captcha;
import com.rabbit.system.service.ICaptchaService;

@RestController
public class CaptchaController {
	@Autowired
	ICaptchaService captchaService;

	@GetMapping("/captcha")
	public AjaxResult getCaptcha() {
		try {
			Captcha cap = captchaService.create();
			cap.setCode("");
			return AjaxResult.success(cap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxResult.error("生成验证码异常");
		}

	}
}
