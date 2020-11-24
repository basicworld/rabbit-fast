package com.rabbit.system.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * 账户常量
 * 
 * @author wlfei
 *
 */
public class AccountConstants {
	public static Set<Integer> VALID_CATEGORY_SET = new HashSet<Integer>() {
		private static final long serialVersionUID = 1L;

		{
			add(CATEGORY_PHONE);
			add(CATEGORY_EMAIL);
			add(CATEGORY_USERNAME);
			add(CATEGORY_IDCARD);
			add(CATEGORY_WECHAT);
		}
	};
	/**
	 * 账户类别：手机号
	 */
	public static final int CATEGORY_PHONE = 1;
	/**
	 * 账户类别：邮箱
	 */
	public static final int CATEGORY_EMAIL = 2;
	/**
	 * 账户类别：用户名
	 */
	public static final int CATEGORY_USERNAME = 3;
	/**
	 * 账户类别：身份证
	 */
	public static final int CATEGORY_IDCARD = 4;
	/**
	 * 账户类别：微信
	 */
	public static final int CATEGORY_WECHAT = 5;
}
