package com.rabbit.common.constant;

/**
 * 基本常量
 * 
 * @author wlfei
 *
 */
public class BaseConstants {
	/**
	 * 已删除
	 */
	public static final int DELETED = 1;
	/**
	 * 未删除
	 */
	public static final int NOT_DELETED = 0;
	/**
	 * 是
	 */
	public static final int YES = 1;
	/**
	 * 否
	 */
	public static final int NO = 0;

	/**
	 * 令牌
	 */
	public static final String TOKEN = "token";

	/**
	 * 令牌前缀
	 */
	public static final String TOKEN_PREFIX = "Bearer ";

	/**
	 * 令牌前缀
	 */
	public static final String LOGIN_USER_KEY = "login_user_key";

	/**
	 * 登录用户 redis key
	 */
	public static final String LOGIN_TOKEN_KEY = "login_tokens:";

	/**
	 * 验证码有效期（分钟）
	 */
	public static final Integer CAPTCHA_EXPIRATION = 5;

	/**
	 * 资源映射路径 前缀
	 */
	public static final String RESOURCE_PREFIX = "/profile";

}
