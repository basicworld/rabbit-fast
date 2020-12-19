package com.rabbit.framework.security.service;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.system.constant.RoleConstants;

/**
 * RuoYi首创 自定义权限实现，ss取自SpringSecurity首字母
 * 
 * @author ruoyi
 */
@Service("ss")
public class PermissionService {
	public static Log logger = LogFactory.getLog(PermissionService.class);
	/** 所有权限标识 */
	private static final String ALL_PERMISSION = "*:*";

	/** 管理员角色权限标识 */
	private static final String SUPER_ADMIN = RoleConstants.ADMIN_ROLE_CODE;

	private static final String ROLE_DELIMETER = ",";

	private static final String PERMISSION_DELIMETER = ",";

	@Autowired
	private TokenService tokenService;

	/**
	 * 验证用户是否具备某权限
	 * 
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	public boolean hasPermi(String permission) {
		if (StringUtils.isEmpty(permission)) {
			return false;
		}
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		if (StringUtils.isNull(loginUser)) {
			return false;
		}
		// 超级管理员具有所有权限
		if (isUserAdmin(loginUser)) {
			return true;
		}
		if (CollectionUtils.isEmpty(loginUser.getPermissions())) {
			return false;
		}
		return hasPermissions(loginUser.getPermissions(), permission);
	}

	/**
	 * 验证用户是否不具备某权限，与 hasPermi逻辑相反
	 *
	 * @param permission 权限字符串
	 * @return 用户是否不具备某权限
	 */
	public boolean lacksPermi(String permission) {
		return hasPermi(permission) != true;
	}

	/**
	 * 验证用户是否具有以下任意一个权限
	 *
	 * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
	 * @return 用户是否具有以下任意一个权限
	 */
	public boolean hasAnyPermi(String permissions) {
		if (StringUtils.isEmpty(permissions)) {
			return false;
		}
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		if (StringUtils.isNull(loginUser)) {
			return false;
		}
		// 超级管理员具有所有权限
		if (isUserAdmin(loginUser)) {
			return true;
		}
		if (CollectionUtils.isEmpty(loginUser.getPermissions())) {
			return false;
		}
		Set<String> authorities = loginUser.getPermissions();
		for (String permission : permissions.split(PERMISSION_DELIMETER)) {
			if (permission != null && hasPermissions(authorities, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断用户是否拥有某个角色
	 * 
	 * @param role 角色字符串
	 * @return 用户是否具备某角色
	 */
	public boolean hasRole(String role) {
		if (StringUtils.isEmpty(role)) {
			return false;
		}
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		if (StringUtils.isNull(loginUser)) {
			return false;
		}
		// 超级管理员具有所有权限
		if (isUserAdmin(loginUser)) {
			return true;
		}
		if (CollectionUtils.isEmpty(loginUser.getRoles())) {
			return false;
		}
		for (String roleCode : loginUser.getRoles()) {
			if (roleCode.equals(StringUtils.trim(role))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证用户是否不具备某角色，与 isRole逻辑相反。
	 *
	 * @param role 角色名称
	 * @return 用户是否不具备某角色
	 */
	public boolean lacksRole(String role) {
		return hasRole(role) != true;
	}

	/**
	 * 验证用户是否具有以下任意一个角色
	 *
	 * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
	 * @return 用户是否具有以下任意一个角色
	 */
	public boolean hasAnyRoles(String roles) {
		if (StringUtils.isEmpty(roles)) {
			return false;
		}
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		if (StringUtils.isNull(loginUser)) {
			return false;
		}
		// 超级管理员具有所有权限
		if (isUserAdmin(loginUser)) {
			return true;
		}
		if (CollectionUtils.isEmpty(loginUser.getRoles())) {
			return false;
		}
		for (String role : roles.split(ROLE_DELIMETER)) {
			if (hasRole(role)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否包含权限
	 * 
	 * @param permissions 权限列表
	 * @param permission  权限字符串
	 * @return 用户是否具备某权限
	 */
	private boolean hasPermissions(Set<String> permissions, String permission) {
		return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
	}

	/**
	 * 判断用书是否为超级管理员
	 * 
	 */
	private boolean isUserAdmin(LoginUser loginUser) {
		if (StringUtils.isEmpty(loginUser.getRoles())) {
			return false;
		}
		for (String roleCode : loginUser.getRoles()) {
			if (SUPER_ADMIN.contains(roleCode)) {
				return true;
			}
		}

		return false;
	}

}
