package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysRoleMenu;

/**
 * 角色--菜单service
 * 
 * @author wlfei
 *
 */
public interface ISysRoleMenuService extends BaseService<SysRoleMenu> {
	/**
	 * 根据角色主键获取列表
	 * 
	 * @param roleId
	 * @return
	 */
	List<SysRoleMenu> listByRoleId(Long roleId);

	/**
	 * 根据角色主键获取列表
	 * 
	 * @param roleIds
	 * @return
	 */
	List<SysRoleMenu> listByRoleId(Long[] roleIds);

	/**
	 * 根据菜单主键获取列表
	 * 
	 * @param menuId
	 * @return
	 */
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
