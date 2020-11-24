package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.SysUserRole;

public interface ISysUserRoleService extends BaseService<SysUserRole> {
	List<SysUserRole> listByUserId(Long userId);

	List<SysUserRole> listByRoleId(Long roleId);

	Integer deleteByUserId(Long userId);

	Integer updateByUser(SysUser user);
}
