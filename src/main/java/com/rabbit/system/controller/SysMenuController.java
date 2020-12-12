package com.rabbit.system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.service.ISysMenuService;

/**
 * 菜单、路由controller
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
	private static final Logger logger = LoggerFactory.getLogger(SysMenuController.class);

	@Autowired
	ISysMenuService menuService;
	@Autowired
	TokenService tokenService;

	/**
	 * 获取菜单列表
	 * 
	 * @param menu
	 * @return
	 */
	@GetMapping("/list")
	public AjaxResult list(SysMenu menu) {
		logger.debug("获取菜单列表...");
		List<SysMenu> menuList = menuService.listByMenu(menu);
		return AjaxResult.success(menuList);
	}

	/**
	 * 获取菜单树
	 * 
	 * @param menu
	 * @return
	 */
	@GetMapping("/tree")
	public AjaxResult treeList(SysMenu menu) {
		logger.debug("获取菜单树...");

		List<SysMenu> menuList = menuService.listByMenu(menu);

		List<SysMenu> treeList = menuService.buildMenuTree(menuList);
		return AjaxResult.success(treeList);
	}

	/**
	 * 获取菜单下拉选择树
	 * 
	 * @param menu
	 * @return
	 */
	@GetMapping("/treeselect")
	public AjaxResult treeSelect(SysMenu menu) {
		logger.debug("获取菜单下拉树...");

		List<SysMenu> menuList = menuService.listByMenu(menu);
		List<TreeSelect> treeSelect = menuService.buildMenuTreeSelect(menuList);
		return AjaxResult.success(treeSelect);
	}

}
