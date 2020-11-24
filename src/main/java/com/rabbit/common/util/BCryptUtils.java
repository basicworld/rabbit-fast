package com.rabbit.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 加密工具
 * 
 * @author wlfei
 *
 */
public class BCryptUtils {
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/**
	 * 加密
	 * 
	 * @param rawPassword
	 * @return
	 */
	public static String encode(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	/**
	 * 判断两个密码是否相同
	 * 
	 * @param rawPassword     未经加密的密码
	 * @param encodedPassword 经过加密的密码
	 * @return
	 */
	public static Boolean isSamePassword(String rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}
}
