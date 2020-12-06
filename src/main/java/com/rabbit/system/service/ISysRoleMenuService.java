package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysRoleMenu;

public interface ISysRoleMenuService extends BaseService<SysRoleMenu> {
	List<SysRoleMenu> listByRoleId(Long roleId);

	List<SysRoleMenu> listByRoleId(Long[] roleIds);

	List<SysRoleMenu> listByMenuId(Long menuId);

	/**
	 * 根据角色ID删除关联关系
	 * 
	 * @param id
	 * @return
	 */
	Integer deleteByRoleId(Long roleId);

	/**
	 * 更新角色的 角色-菜单 关联关系
	 * 
	 * @param item
	 * @return
	 */
	Integer updateByRole(SysRole role);

	/**
	 * 为角色新增 角色-菜单 关联关系
	 * 
	 * @param item
	 * @return
	 */
	Integer insertByRole(SysRole role);

}
