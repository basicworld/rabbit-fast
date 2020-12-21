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
	 * 使用缓存配置，更新邮箱配置参数和邮箱连接<br>
	 * 一般用于动态修改smtp相关参数
	 * 
	 * @return
	 */
	Boolean reloadMailConfigFromCache();

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
