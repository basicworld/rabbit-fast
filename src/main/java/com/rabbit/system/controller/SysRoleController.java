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

import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TableDataInfo;
import com.rabbit.system.base.BaseController;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.service.ISysRoleService;

@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
	@Autowired
	ISysRoleService roleService;

	/**
	 * 新增
	 * 
	 * @param role
	 * @return
	 */
	@PostMapping
	public AjaxResult add(@Validated @RequestBody SysRole role) {
		ValidResult result = roleService.validCheckBeforeInsert(role);
		if (result.hasError()) {
			return AjaxResult.error(result.getMessage());
		}
		roleService.insertSelective(role);
		return AjaxResult.success();
	}

	/**
	 * 删除
	 * 
	 * @param roleId
	 * @return
	 */
	@DeleteMapping("/{roleId}")
	public AjaxResult delete(@PathVariable Long roleId) {
		ValidResult result = roleService.validCheckBeforeDelete(roleId);
		if (result.hasError()) {
			return AjaxResult.error(result.getMessage());
		}
		roleService.deleteByPrimaryKey(roleId);
		return AjaxResult.success();
	}

	/**
	 * 列表查询
	 * 
	 * @param role
	 * @return
	 */
	@GetMapping("/list")
	public TableDataInfo list(@RequestBody SysRole role) {
		startPage();
		List<SysRole> roleList = roleService.listByRole(role);
		return getDataTable(roleList);
	}

	/**
	 * 更新
	 * 
	 * @param role
	 * @return
	 */
	@PutMapping
	public AjaxResult update(@Validated @RequestBody SysRole role) {
		ValidResult result = roleService.validCheckBeforeUpdate(role);
		if (result.hasError()) {
			return AjaxResult.error(result.getMessage());
		}
		roleService.updateSelective(role);
		return AjaxResult.success();
	}

}
