package com.rabbit.system.service;

import com.rabbit.common.util.valid.ValidResult;

/**
 * 邮箱service
 * 
 * @author wlfei
 *
 */
public interface IMailService {
	/**
	 * 发送纯文本邮件
	 * 
	 * @param to
	 * @param subject
	 * @param content
	 * @return
	 */
	ValidResult sendSimpleMail(String to, String subject, String content);

}
