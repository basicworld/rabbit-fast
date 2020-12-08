package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptUser;
import com.rabbit.system.domain.SysUser;

/**
 * 部门--用户service
 * 
 * @author wlfei
 *
 */
public interface ISysDeptUserService extends BaseService<SysDeptUser> {
	/**
	 * 根据部门主键获取部门--用户关联列表
	 * 
	 * @param deptId
	 * @return
	 */
	List<SysDeptUser> listByDeptId(Long deptId);

	/**
	 * 根据用户主键删除
	 * 
	 * @param userId
	 * @return
	 */
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

	/**
	 * 为用户插入部门--用户关联关系
	 * 
	 * @param user
	 * @return
	 */
	Integer insertByUser(SysUser user);

	/**
	 * 为用户更新部门--用户关联关系
	 * 
	 * @param user
	 * @return
	 */
	Integer updateByUser(SysUser user);

	/**
	 * 根据用户ID查询部门关联，逻辑前提：每个用户只属于一个部门
	 * 
	 * @param userId
	 * @return
	 */
	SysDeptUser selectByUserId(Long userId);
}
