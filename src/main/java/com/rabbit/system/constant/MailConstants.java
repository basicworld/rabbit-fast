package com.rabbit.system.constant;

/**
 * 邮箱常量
 * 
 * @author wlfei
 *
 */
public class MailConstants {
	/** 测试邮件标题 */
	public static final String SUBJECT_OF_TEST = "参数验证邮件";
	/** 测试邮件内容 */
	public static final String CONTENT_OF_TEST = "收到邮件表示参数配置成功。";

	/** 用户注册成功邮件标题 */
	public static final String SUBJECT_OF_USER_REGIST_SUCCESS = "用户注册通知";
	/** 用户注册成功邮件内容，需要传入一个参数：用户邮箱 */
	public static final String CONTENT_OF_USER_REGIST_SUCCESS = "您邮箱{}注册账号成功。";

}
