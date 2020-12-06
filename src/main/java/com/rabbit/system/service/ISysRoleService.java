package com.rabbit.system.service;

import java.util.List;

import com.rabbit.common.util.valid.ValidCheckService;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysRole;

public interface ISysRoleService extends BaseService<SysRole>, ValidCheckService<SysRole> {
	/**
	 * 根据角色名称等值查询
	 * 
	 * @param name
	 * @return
	 */
	List<SysRole> listByNameEqualsTo(String name);

	/**
	 * 根据角色代码等值查询
	 * 
	 * @param code
	 * @return
	 */
	List<SysRole> listByCodeEqualsTo(String code);

	/**
	 * 根据角色名称、角色代码模糊查询，可指定删除状态
	 * 
	 * @param role
	 * @return
	 */
	List<SysRole> listByRole(SysRole role);

	/**
	 * 批量删除
	 * 
	 * @param roleIds
	 * @return
	 */
	Integer deleteByPrimaryKey(Long[] roleIds);

	/**
	 * 根据主键批量查询
	 * 
	 * @param roleIds
	 * @return
	 */
	List<SysRole> listByPrimaryKeys(Long[] roleIds);

	/**
	 * 根据用户ID查询所有角色<br>
	 * 
	 * @param userId
	 * @return 用户自己的角色+用户所属部门的角色
	 */
	List<SysRole> listByUserId(Long userId);

}
