package com.rabbit.system.service;

import java.util.List;

import com.rabbit.common.util.valid.ValidCheckService;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;

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
	 * 根据用户主键逻辑删除用户
	 * 
	 * @param id
	 * @return
	 */
	Integer deleteByPrimaryKeyLogically(Long id);

}
