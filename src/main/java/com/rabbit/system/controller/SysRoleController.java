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
import com.rabbit.framework.aspectj.annotation.Log;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TableDataInfo;
import com.rabbit.system.base.BaseController;
import com.rabbit.system.constant.LogConstants;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysRoleMenu;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.service.ISysMenuService;
import com.rabbit.system.service.ISysRoleMenuService;
import com.rabbit.system.service.ISysRoleService;
import com.rabbit.system.service.ISysUserRoleService;

/**
 * 角色controller
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);

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
	 * 新增角色
	 * 
	 * @param role
	 * @return
	 */
	@Log(operateType = LogConstants.TYPE_ADD_ROLE)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody SysRole role) {
		logger.debug("新增角色...");
		ValidResult result = roleService.validCheckBeforeInsert(role);
		if (result.hasError()) {
			return AjaxResult.error(result.getMessage());
		}
		roleService.insertSelective(role);
		return AjaxResult.success();
	}

	/**
	 * 删除角色
	 * 
	 * @param roleIds 角色ID列表
	 * @return
	 */
	@Log(operateType = LogConstants.TYPE_DEL_ROLE)
	@DeleteMapping("/{roleIds}")
	public AjaxResult delete(@PathVariable Long[] roleIds) {
		logger.debug("删除角色：" + roleIds);
		// 校验每个角色是否可删除，如果存在不能删除的，则不删除任何角色
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
	 * 获取角色详情
	 * 
	 * @param roleId
	 * @return
	 */
	@GetMapping("/{roleId}")
	public AjaxResult getDetail(@PathVariable Long roleId) {
		logger.debug("获取角色详情...");

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
		// 将叶子结点菜单ID返回给前端
		role.setMenuIds(menuLeafNodeIds);
		return AjaxResult.success(role);
	}

	/**
	 * 角色列表查询
	 * 
	 * @param role
	 * @return
	 */
	@GetMapping("/list")
	public TableDataInfo list(SysRole role) {
		logger.debug("获取角色列表...");

		startPage();
		List<SysRole> roleList = roleService.listByRole(role);
		return getDataTable(roleList);
	}

	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	@Log(operateType = LogConstants.TYPE_EDIT_ROLE)
	@PutMapping
	public AjaxResult update(@Validated @RequestBody SysRole role) {
		logger.debug("更新角色...");

		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUser user = loginUser.getUser();
		// 当前登录用户的角色set
		Set<Long> roleIdOfUser = roleService.listByUserId(user.getId()).stream().map(v -> v.getId())
				.collect(Collectors.toSet());
		// 如果待修改的角色被当前登录用户使用，则登录用户不能修改该角色
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
