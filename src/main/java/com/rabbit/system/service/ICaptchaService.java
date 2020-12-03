package com.rabbit.system.service;

import java.io.IOException;

import com.rabbit.system.domain.Captcha;

/**
 * 验证码
 * 
 * @author wlfei
 *
 */
public interface ICaptchaService {
	/**
	 * 创建验证码
	 * 
	 * @return
	 * @throws IOException
	 */
	Captcha create() throws IOException;

	/**
	 * 验证验证码是否正确
	 * 
	 * @param item
	 * @return
	 */
	Boolean validate(Captcha item);

}
