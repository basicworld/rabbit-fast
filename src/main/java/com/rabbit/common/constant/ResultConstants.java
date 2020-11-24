package com.rabbit.common.constant;

public class ResultConstants {
	/**
	 * 成功
	 */
	public static final int CODE_SUCCESS = 20000;

	/**
	 * 用户端错误
	 */
	public static final int CODE_USER_ERROR = 40000;

	/**
	 * token错误
	 */
	public static final int CODE_ILLEGAL_TOKEN = 50008;

	/**
	 * 鉴权错误
	 */
	public static final int CODE_AUTH_FAIL = 50016;

	/**
	 * 服务端错误
	 */
	public static final int CODE_SERVER_ERROR = 50000;

	public static final String MESSAGE_SUCCESS = "success";
	public static final String MESSAGE_USER_ERROR = "user error";
	public static final String MESSAGE_SERVER_ERROR = "server error";
}
