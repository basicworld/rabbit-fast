package com.rabbit.system.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TableDataInfo;
import com.rabbit.system.base.BaseController;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysRoleMenu;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.service.ISysMenuService;
import com.rabbit.system.service.ISysRoleMenuService;
import com.rabbit.system.service.ISysRoleService;
import com.rabbit.system.service.ISysUserRoleService;

@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
	protected final Logger logger = LoggerFactory.getLogger(SysRoleController.class);

	@Autowired
	ISysRoleService roleService;

	@Autowired
	ISysRoleMenuService roleMenuService;

	@Autowired
	ISysMenuService menuService;

	@Autowired
	ISysUserRoleService userRoleService;

	@Autowired
	TokenService tokenService;

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
	@DeleteMapping("/{roleIds}")
	public AjaxResult delete(@PathVariable Long[] roleIds) {
		logger.debug("执行删除：" + roleIds);
		for (Long roleId : roleIds) {
			ValidResult result = roleService.validCheckBeforeDelete(roleId);
			if (result.hasError()) {
				return AjaxResult.error(result.getMessage());
			}
		}

		roleService.deleteByPrimaryKey(roleIds);
		return AjaxResult.success();
	}

	/**
	 * 获取详情
	 * 
	 * @param roleId
	 * @return
	 */
	@GetMapping("/{roleId}")
	public AjaxResult getDetail(@PathVariable Long roleId) {
		SysRole role = roleService.selectByPrimaryKey(roleId);

		if (StringUtils.isNull(role)) {
			return AjaxResult.error("角色不存在");
		}
		List<SysRoleMenu> roleMenus = roleMenuService.listByRoleId(roleId);
		// 获取角色关联菜单ID
		Long[] menuIds = roleMenus.stream().map(v -> v.getMenuId()).toArray(Long[]::new);

		// 筛选所有叶子结点菜单ID
		List<SysMenu> allMenus = menuService.listByMenu(new SysMenu());
		Set<Long> allMenuParentIds = allMenus.stream().map(v -> v.getParentId()).collect(Collectors.toSet());
		Long[] menuLeafNodeIds = Stream.of(menuIds).filter(v -> !allMenuParentIds.contains(v)).toArray(Long[]::new);

		role.setMenuIds(menuLeafNodeIds);
		return AjaxResult.success(role);
	}

	/**
	 * 列表查询
	 * 
	 * @param role
	 * @return
	 */
	@GetMapping("/list")
	public TableDataInfo list(SysRole role) {
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
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUser user = loginUser.getUser();
		Set<Long> roleIdOfUser = roleService.listByUserId(user.getId()).stream().map(v -> v.getId())
				.collect(Collectors.toSet());
		if (roleIdOfUser.contains(role.getId())) {
			return AjaxResult.error("不允许修改本人的关联权限");
		}
		ValidResult result = roleService.validCheckBeforeUpdate(role);
		if (result.hasError()) {
			return AjaxResult.error(result.getMessage());
		}
		roleService.updateSelective(role);
		return AjaxResult.success();
	}

}
