package com.rabbit.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.constant.DeptConstants;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.service.ISysDeptService;

@RestController
@RequestMapping("/system/dept")
public class SysDeptController {
	@Autowired
	ISysDeptService deptService;

	@GetMapping("/list")
	public AjaxResult list(SysDept dept) {
		List<SysDept> itemList = deptService.listByDept(dept);
		return AjaxResult.success(itemList);
	}

	@GetMapping("/tree")
	public AjaxResult treeList(SysDept dept) {
		List<SysDept> itemList = deptService.listByDept(dept);
		List<SysDept> treeList = deptService.buildDeptTree(itemList);
		return AjaxResult.success(treeList);
	}

	@GetMapping("/treeselect")
	public AjaxResult treeSelect(SysDept dept) {
		List<SysDept> itemList = deptService.listByDept(dept);
		List<TreeSelect> treeSelect = deptService.buildDeptTreeSelect(itemList);
		return AjaxResult.success(treeSelect);
	}

	/**
	 * 新增
	 * 
	 * @param dept
	 * @return
	 */
	@PostMapping
	public AjaxResult add(@Validated @RequestBody SysDept dept) {
		if (StringUtils.isNull(dept.getParentId())) {
			dept.setParentId(DeptConstants.ROOT_DEPT_ID);
		}
		ValidResult checkResult = deptService.validCheckBeforeInsert(dept);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		deptService.insertSelective(dept);
		return AjaxResult.success();
	}

	@DeleteMapping("/{deptId}")
	public AjaxResult delete(@PathVariable Long deptId) {
		ValidResult checkResult = deptService.validCheckBeforeDelete(deptId);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		deptService.deleteByPrimaryKey(deptId);
		return AjaxResult.success();
	}

	/**
	 * 更新
	 * 
	 * @param dept
	 * @return
	 */
	@PutMapping
	public AjaxResult update(@Validated @RequestBody SysDept dept) {
		ValidResult checkResult = deptService.validCheckBeforeUpdate(dept);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		deptService.updateSelective(dept);
		return AjaxResult.success();
	}

}
