package com.rabbit.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.domain.dto.SysRouter;
import com.rabbit.system.service.ISysMenuService;

@RestController
@RequestMapping()
public class SysRouterController {
	@Autowired
	ISysMenuService menuService;

	@GetMapping("/getRouters")
	public AjaxResult getRouters() {
		// todo: 根据用户信息获取菜单列表

		List<SysMenu> menuList = menuService.listByMenu(new SysMenu());
		List<SysMenu> treeList = menuService.buildMenuTree(menuList);
		List<SysRouter> routerList = menuService.menu2Router(treeList);
		return AjaxResult.success(routerList);
	}

}
