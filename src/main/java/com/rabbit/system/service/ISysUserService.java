package com.rabbit.system.service;

import java.util.List;

import com.rabbit.common.util.valid.ValidCheckService;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;

public interface ISysUserService extends BaseService<SysUser>, ValidCheckService<SysUser> {

	/**
	 * SysAccount to SysUser
	 * 
	 * @param accountList
	 * @return
	 */
	List<SysUser> account2User(List<SysAccount> accountList);

	/**
	 * SysAccount to SysUser
	 * 
	 * @param accountList
	 * @return
	 */
	SysUser account2User(SysAccount account);

	/**
	 * 前端传回的dto转为user
	 * 
	 * @param userDTO
	 * @return
	 */
	SysUser dto2User(SysUserDTO userDTO);

	/**
	 * user转为前端需要的dto
	 * 
	 * @param user
	 * @return
	 */
	SysUserDTO user2dto(SysUser user);

	/**
	 * 根据用户主键逻辑删除用户
	 * 
	 * @param id
	 * @return
	 */
	Integer deleteByPrimaryKeyLogically(Long id);

	/**
	 * 根据用户主键逻辑删除用户 批量
	 * 
	 * @param id
	 * @return
	 */
	Integer deleteByPrimaryKeyLogically(Long[] userIds);

	/**
	 * 根据前端条件 模糊查询
	 * 
	 * @param userDTO
	 * @return
	 */
	List<SysUser> listByUserDTO(SysUserDTO userDTO);

	/**
	 * 根据主键批量删除用户
	 * 
	 * @param userIds
	 * @return
	 */
	Integer deleteByPrimaryKey(Long[] userIds);

	/**
	 * 判断是否是超级管理员
	 * 
	 * @param userId
	 * @return
	 */
	Boolean isAdmin(Long userId);

	Boolean isNotAdmin(Long userId);

}
