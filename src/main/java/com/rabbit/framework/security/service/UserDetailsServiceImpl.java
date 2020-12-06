package com.rabbit.framework.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.StringUtils;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.system.constant.AccountConstants;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysDeptRoleService;
import com.rabbit.system.service.ISysDeptService;
import com.rabbit.system.service.ISysDeptUserService;
import com.rabbit.system.service.ISysMenuService;
import com.rabbit.system.service.ISysRoleMenuService;
import com.rabbit.system.service.ISysRoleService;
import com.rabbit.system.service.ISysUserRoleService;
import com.rabbit.system.service.ISysUserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	public static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	@Autowired
	ISysUserService userService;
	@Autowired
	ISysRoleService roleService;
	@Autowired
	ISysAccountService accountService;
	@Autowired
	ISysUserRoleService userRoleService;
	@Autowired
	ISysDeptService deptService;
	@Autowired
	ISysDeptUserService deptUserService;
	@Autowired
	ISysDeptRoleService deptRoleService;
	@Autowired
	ISysRoleMenuService roleMenuService;
	@Autowired
	ISysMenuService menuService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysAccount queryParam = new SysAccount();
		queryParam.setOpenCode(username);
		// 根据用户名查询用户
		List<SysAccount> accounts = new ArrayList<SysAccount>();
		for (int category : AccountConstants.VALID_CATEGORY_SET) {
			queryParam.setCategory(category);
			accounts = accountService.listByCategoryAndOpenCode(queryParam);
			if (StringUtils.isNotEmpty(accounts)) {
				break;
			}
		}
		if (accounts.size() < 1) {
			throw new UsernameNotFoundException("用户名不存在：" + username);
		}

		SysUser user = userService.account2User(accounts.get(0));
		if (null == user) {
			throw new UsernameNotFoundException("用户不存在");
		}

		List<SysRole> roles = roleService.listByUserId(user.getId());
		Long[] roleIds = roles.stream().map(v -> v.getId()).toArray(Long[]::new);
		user.setRoleIds(roleIds);
		// 所有角色代码
		Set<String> roleCodes = roles.stream().map(v -> v.getCode()).collect(Collectors.toSet());

		Long[] menuIds = roleMenuService.listByRoleId(roleIds).stream().map(v -> v.getMenuId()).toArray(Long[]::new);
		// 所有权限代码
		Set<String> menuPerms = menuService.listByPrimaryKeys(menuIds).stream().map(v -> v.getPerms())
				.filter(v -> StringUtils.isNotNull(v)).collect(Collectors.toSet());

		LoginUser loginUser = new LoginUser();
		loginUser.setUser(user);
		loginUser.setRoles(roleCodes);
		loginUser.setPermissions(menuPerms);
		return loginUser;
	}

}
