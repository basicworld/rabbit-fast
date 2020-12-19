package com.rabbit.system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.aspectj.annotation.Log;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.constant.DeptConstants;
import com.rabbit.system.constant.LogConstants;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptRole;
import com.rabbit.system.service.ISysDeptRoleService;
import com.rabbit.system.service.ISysDeptService;
import com.rabbit.system.service.ISysRoleService;
import com.rabbit.system.service.ISysUserService;

/**
 * 部门controller
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController {
	private static final Logger logger = LoggerFactory.getLogger(SysDeptController.class);

	@Autowired
	ISysDeptService deptService;
	@Autowired
	ISysRoleService roleService;
	@Autowired
	TokenService tokenService;
	@Autowired
	ISysUserService userService;

	@Autowired
	ISysDeptRoleService deptRoleService;

	/**
	 * 获取部门列表
	 * 
	 * @param dept
	 * @return
	 */
	@GetMapping("/list")
	public AjaxResult list(SysDept dept) {
		logger.debug("获取部门列表...");
		List<SysDept> itemList = deptService.listByDept(dept);
		return AjaxResult.success(itemList);
	}

	/**
	 * 获取部门详情
	 * 
	 * @param deptId
	 * @return
	 */
	@GetMapping("/{deptId}")
	public AjaxResult getDetail(@PathVariable Long deptId) {
		logger.debug("获取部门详情...");

		SysDept dept = deptService.selectByPrimaryKey(deptId);
		if (StringUtils.isNull(dept)) {
			return AjaxResult.error("部门不存在");
		}
		List<SysDeptRole> deptRoleList = deptRoleService.listByDeptId(deptId);
		Long[] roleIds = deptRoleList.stream().map(v -> v.getRoleId()).toArray(Long[]::new);
		dept.setRoleIds(roleIds);
		return AjaxResult.success(dept);
	}

	/**
	 * 获取部门树
	 * 
	 * @param dept
	 * @return
	 */
	@GetMapping("/tree")
	public AjaxResult treeList(SysDept dept) {
		logger.debug("获取部门树...");

		List<SysDept> itemList = deptService.listByDept(dept);
		List<SysDept> treeList = deptService.buildDeptTree(itemList);
		return AjaxResult.success(treeList);
	}

	/**
	 * 获取部门下拉菜单树
	 * 
	 * @param dept
	 * @return
	 */
	@GetMapping("/treeselect")
	public AjaxResult treeSelect(SysDept dept) {
		logger.debug("获取部门下拉树...");

		List<SysDept> itemList = deptService.listByDept(dept);
		List<TreeSelect> treeSelect = deptService.buildDeptTreeSelect(itemList);
		return AjaxResult.success(treeSelect);
	}

	/**
	 * 新增部门<br>
	 * 限制：登录用户非管理员的，不允许创建管理员部门
	 * 
	 * @param dept
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:dept')")
	@Log(operateType = LogConstants.TYPE_ADD_DEPT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody SysDept dept) {
		logger.debug("新增部门...");

		// 登录用户非管理员的，不能创建包含管理员角色的部门
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		boolean loginUserNotAdmin = userService.isNotAdmin(loginUser.getUser().getId());
		if (loginUserNotAdmin) {
			boolean containAdminRole = roleService.isContainsAdminRole(dept.getRoleIds());
			if (containAdminRole) {
				return AjaxResult.error("非管理员用户不能创建包含管理员角色的部门");
			}
		}
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

	/**
	 * 删除<br>
	 * 限制：登录用户非管理员的，不允许删除管理员部门
	 * 
	 * @param deptId
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:dept')")
	@Log(operateType = LogConstants.TYPE_DEL_DEPT)
	@DeleteMapping("/{deptId}")
	public AjaxResult delete(@PathVariable Long deptId) {
		logger.debug("删除部门...");

		// 登录用户非管理员的，不能删除包含管理员角色的部门
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		boolean loginUserNotAdmin = userService.isNotAdmin(loginUser.getUser().getId());
		if (loginUserNotAdmin) {
			boolean deptIsAdmin = deptService.isAdminDept(deptId);
			if (deptIsAdmin) {
				return AjaxResult.error("非管理员用户不能删除包含管理员角色的部门");
			}
		}
		ValidResult checkResult = deptService.validCheckBeforeDelete(deptId);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		deptService.deleteByPrimaryKey(deptId);
		return AjaxResult.success();
	}

	/**
	 * 更新<br>
	 * 限制：登录用户非管理员的，不允许修改管理员部门，不允许给部门添加管理员角色
	 * 
	 * @param dept
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:dept')")
	@Log(operateType = LogConstants.TYPE_EDIT_DEPT)
	@PutMapping
	public AjaxResult update(@Validated @RequestBody SysDept dept) {
		logger.debug("更新部门...");

		// 登录用户非管理员的，不能更新包含管理员角色的部门，不能给部门添加管理员角色
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		boolean loginUserNotAdmin = userService.isNotAdmin(loginUser.getUser().getId());
		if (loginUserNotAdmin) {
			boolean deptIsAdmin = deptService.isAdminDept(dept.getId());
			if (deptIsAdmin) {
				return AjaxResult.error("非管理员用户不能更新包含管理员角色的部门");
			}

			boolean containAdminRole = roleService.isContainsAdminRole(dept.getRoleIds());
			if (containAdminRole) {
				return AjaxResult.error("不能给部门添加管理员角色");
			}
		}

		ValidResult checkResult = deptService.validCheckBeforeUpdate(dept);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		deptService.updateSelective(dept);
		return AjaxResult.success();
	}

}
