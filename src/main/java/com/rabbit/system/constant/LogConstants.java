package com.rabbit.system.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志常量
 * 
 * @author wlfei
 *
 */
public class LogConstants {

	/**
	 * 所有日志操作类型set
	 */
	public static List<String> ALL_OPER_TYPE_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(TYPE_LOGIN);
			add(TYPE_CHANGE_PWD);
			add(TYPE_CHANGE_INFO);
			add(TYPE_LOGOUT);

			add(TYPE_ADD_USER);
			add(TYPE_DEL_USER);
			add(TYPE_EDIT_USER);
			add(TYPE_RESET_PWD);

			add(TYPE_ADD_ROLE);
			add(TYPE_DEL_ROLE);
			add(TYPE_EDIT_ROLE);

			add(TYPE_ADD_DEPT);
			add(TYPE_DEL_DEPT);
			add(TYPE_EDIT_DEPT);

			add(TYPE_EDIT_CONFIG);
			add(TYPE_RECACHE_CONFIG);

			add(TYPE_OTHER);
		}

	};

	// 操作类型：登录注册类
	/** 登录 */
	public static final String TYPE_LOGIN = "用户登录";
	/** 修改密码 */
	public static final String TYPE_CHANGE_PWD = "用户修改密码";
	/** 修改信息 */
	public static final String TYPE_CHANGE_INFO = "用户修改信息";
	/** 退出登录 */
	public static final String TYPE_LOGOUT = "用户登出";

	// 操作类型：管理员操作类
	/** 新增用户 */
	public static final String TYPE_ADD_USER = "新增用户";
	/** 删除用户 */
	public static final String TYPE_DEL_USER = "删除用户";
	/** 编辑用户 */
	public static final String TYPE_EDIT_USER = "编辑用户";
	/** 重置密码 */
	public static final String TYPE_RESET_PWD = "重置密码";

	/** 新增角色 */
	public static final String TYPE_ADD_ROLE = "新增角色";
	/** 删除角色 */
	public static final String TYPE_DEL_ROLE = "删除角色";
	/** 编辑角色 */
	public static final String TYPE_EDIT_ROLE = "编辑角色";

	/** 新增部门 */
	public static final String TYPE_ADD_DEPT = "新增部门";
	/** 删除部门 */
	public static final String TYPE_DEL_DEPT = "删除部门";
	/** 编辑部门 */
	public static final String TYPE_EDIT_DEPT = "编辑部门";

	/** 配置系统参数 */
	public static final String TYPE_EDIT_CONFIG = "配置系统参数";
	/** 刷新系统参数 */
	public static final String TYPE_RECACHE_CONFIG = "刷新系统参数";

	public static final String TYPE_OTHER = "其他操作";

}
