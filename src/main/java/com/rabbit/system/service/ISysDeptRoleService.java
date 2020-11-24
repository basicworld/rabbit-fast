package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptRole;

public interface ISysDeptRoleService extends BaseService<SysDeptRole> {
	/**
	 * 根据部门ID等值查询
	 * 
	 * @param deptId
	 * @return
	 */
	List<SysDeptRole> listByDeptId(Long deptId);

	/**
	 * 根据角色ID等值查询
	 * 
	 * @param roleId
	 * @return
	 */
	List<SysDeptRole> listByRoleId(Long roleId);

	/**
	 * 根据部门ID和角色ID去重判断，如果存在则不插入，不存在则插入
	 * 
	 * @param deptRole
	 * @return
	 */
	Integer insertIfNotExists(SysDeptRole deptRole);

	/**
	 * 为部门插入角色关联，用于新建部门<br>
	 * 对每个部门-角色关联，执行insert
	 * 
	 * @param item
	 * @return
	 */
	Integer insertByDept(SysDept dept);

	/**
	 * 为部门更新角色关联，用于更新部门<br>
	 * 
	 * 
	 * @param item
	 * @return
	 */
	Integer updateByDept(SysDept dept);
}
