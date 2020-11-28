package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptUser;
import com.rabbit.system.domain.SysUser;

public interface ISysDeptUserService extends BaseService<SysDeptUser> {
	List<SysDeptUser> listByDeptId(Long deptId);

	Integer deleteByUserId(Long userId);

	/**
	 * 为部门插入用户关联
	 * 
	 * @param dept
	 * @return
	 */
	Integer insertByDept(SysDept dept);

	/**
	 * 为部门更新用户关联
	 * 
	 * @param dept
	 * @return
	 */
	Integer updateByDept(SysDept dept);

	Integer insertByUser(SysUser user);

	Integer updateByUser(SysUser user);

	/**
	 * 根据用户ID查询部门关联，逻辑前提：每个用户只属于一个部门
	 * 
	 * @param userId
	 * @return
	 */
	SysDeptUser selectByUserId(Long userId);
}
