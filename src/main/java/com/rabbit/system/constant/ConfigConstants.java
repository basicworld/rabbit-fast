package com.rabbit.system.constant;

/**
 * 系统配置常量
 * 
 * @author wlfei
 *
 */
public class ConfigConstants {
	/** name of PropertiesPropertySource */
	public static final String NAME_OF_DB_PROPERTY_SOURCE = "system-config";
	/** SMTP 配置键名：邮箱服务器 */
	public static final String KEY_OF_MAIL_SMTP_HOST = "spring.mail.host";
	/** SMTP 配置键名：邮箱服务器端口 */
	public static final String KEY_OF_MAIL_SMTP_PORT = "spring.mail.port";
	/** SMTP 配置键名：邮箱是否使用ssl协议 */
	public static final String KEY_OF_MAIL_SMTP_USE_SSL = "spring.mail.properties.mail.smtp.ssl.enable";
	/** SMTP 配置键名：邮箱用户名 */
	public static final String KEY_OF_MAIL_SMTP_USERNAME = "spring.mail.username";
	/** SMTP 配置键名：邮箱密码/授权码 */
	public static final String KEY_OF_MAIL_SMTP_PASSWORD = "spring.mail.password";
	/** SMTP 配置键名：邮箱发信人昵称 */
	public static final String KEY_OF_MAIL_SMTP_FROM = "spring.mail.from";

}
