package com.rabbit.system.service;

import java.io.IOException;

import com.rabbit.system.domain.Captcha;

/**
 * 验证码service
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
	 * @param item {uuid:xxx, code:xxx}
	 * @return
	 */
	Boolean validate(Captcha item);

}
