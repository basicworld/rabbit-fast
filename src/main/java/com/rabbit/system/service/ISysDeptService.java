package com.rabbit.system.service;

import java.util.List;

import com.rabbit.common.util.valid.ValidCheckService;
import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysDept;

public interface ISysDeptService extends BaseService<SysDept>, ValidCheckService<SysDept> {
	/**
	 * 根据机构全称或简称 等值查询
	 * 
	 * @param name 机构全称或简称
	 * @return
	 */
	List<SysDept> listByNameEqualsTo(String name);

	/**
	 * 根据机构代码等值查询
	 * 
	 * @param orgcode
	 * @return
	 */
	List<SysDept> listByOrgcodeEqualsTo(String orgcode);

	/**
	 * 多参数查询：简称模糊查询+全称模糊查询+机构代码模糊查询+是否已删除
	 * 
	 * @param dept
	 * @return
	 */
	List<SysDept> listByDept(SysDept dept);

	/**
	 * 构建前段所需的树结构
	 * 
	 * @param deptList
	 * @return 机构树
	 */
	List<SysDept> buildDeptTree(List<SysDept> deptList);

	/**
	 * 构建前段所需的下拉树结构
	 * 
	 * @param deptList
	 * @return 下拉树
	 */
	List<TreeSelect> buildDeptTreeSelect(List<SysDept> deptList);

}
