package com.rabbit.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.service.ISysMenuService;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
	@Autowired
	ISysMenuService menuService;

	@GetMapping("/list")
	public AjaxResult list(SysMenu menu) {
		List<SysMenu> menuList = menuService.listByMenu(menu);
		return AjaxResult.success(menuList);
	}

	@GetMapping("/tree")
	public AjaxResult treeList(SysMenu menu) {
		List<SysMenu> menuList = menuService.listByMenu(menu);
		List<SysMenu> treeList = menuService.buildMenuTree(menuList);
		return AjaxResult.success(treeList);
	}

	@GetMapping("/treeselect")
	public AjaxResult treeSelect(SysMenu menu) {
		List<SysMenu> menuList = menuService.listByMenu(menu);
		List<TreeSelect> treeSelect = menuService.buildMenuTreeSelect(menuList);
		return AjaxResult.success(treeSelect);
	}

}
