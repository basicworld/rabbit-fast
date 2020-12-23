package com.rabbit.system.constant;

/**
 * 系统配置常量
 * 
 * @author wlfei
 *
 */
public class ConfigConstants {
	public static final String STRING_TRUE = "true";
	public static final String STRING_FALSE = "false";

	/** 令牌自定义标识 */
	public static final String KEY_OF_TOKEN_HEADER = "my.token.header";
	/** 令牌秘钥 */
	public static final String KEY_OF_TOKEN_SECRET = "my.token.secret";
	/** 令牌有效期(分钟) */
	public static final String KEY_OF_TOKEN_EXPIRE_TIME = "my.token.expire_time";
	/** 新创建用户默认密码 */
	public static final String KEY_OF_USER_DEFAULT_PASSWORD = "my.user.default_password";

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
	/** SMTP 配置键名：开启发邮件功能 */
	public static final String KEY_OF_MAIL_OPEN = "my.mail.smtp.open";

	/** 邮件标题键名：新增用户成功 */
	public static final String KEY_OF_MAIL_SUBJECT_OF_ADD_USER_SUCCESS = "my.mail.subject.user_add_success";
	/** 邮件内容键名：新增用户成功 */
	public static final String KEY_OF_MAIL_CONTENT_OF_ADD_USER_SUCCESS = "my.mail.content.user_add_success";

	/** 邮件标题键名：修改用户成功 */
	public static final String KEY_OF_MAIL_SUBJECT_OF_EDIT_USER_SUCCESS = "my.mail.subject.user_edit_success";
	/** 邮件内容键名：修改用户成功 */
	public static final String KEY_OF_MAIL_CONTENT_OF_EDIT_USER_SUCCESS = "my.mail.content.user_edit_success";

	/** 邮件标题键名：重置密码成功 */
	public static final String KEY_OF_MAIL_SUBJECT_OF_RESET_PASSWORD_SUCCESS = "my.mail.subject.reset_password_success";
	/** 邮件内容键名：重置密码成功 */
	public static final String KEY_OF_MAIL_CONTENT_OF_RESET_PASSWORD_SUCCESS = "my.mail.content.reset_password_success";

	/** 文件保存地址 */
	public static final String KEY_OF_FILE_SAVE_DIR = "my.file.save.basedir";
}
