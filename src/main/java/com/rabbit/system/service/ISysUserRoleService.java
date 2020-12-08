package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.SysUserRole;

/**
 * 用户--角色service
 * 
 * @author wlfei
 *
 */
public interface ISysUserRoleService extends BaseService<SysUserRole> {
	/**
	 * 根据用户主键获取关联关系
	 * 
	 * @param userId
	 * @return
	 */
	List<SysUserRole> listByUserId(Long userId);

	/**
	 * 根据角色主键获取关联关系
	 * 
	 * @param roleId
	 * @return
	 */
	List<SysUserRole> listByRoleId(Long roleId);

	/**
	 * 根据用户主键删除关联关系
	 * 
	 * @param userId
	 * @return
	 */
	Integer deleteByUserId(Long userId);

	/**
	 * 为用户更新关联关系
	 * 
	 * @param user
	 * @return
	 */
	Integer updateByUser(SysUser user);

	/**
	 * 为用户新增用户--角色关联关系
	 * 
	 * @param user
	 * @return
	 */
	Integer insertByUser(SysUser user);
}
