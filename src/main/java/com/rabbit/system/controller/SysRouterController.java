package com.rabbit.system.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.common.util.ServletUtils;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysRouter;
import com.rabbit.system.service.ISysMenuService;

@RestController
@RequestMapping()
public class SysRouterController {

	protected final Logger logger = LoggerFactory.getLogger(SysRouterController.class);

	@Autowired
	ISysMenuService menuService;
	@Autowired
	TokenService tokenService;

	@GetMapping("/getRouters")
	public AjaxResult getRouters() {
		// todo: 根据用户信息获取菜单列表
		logger.debug("获取路由...");
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUser user = loginUser.getUser();
		List<SysMenu> menuList = menuService.listByUserId(user.getId());
		logger.debug("所获取的菜单ID list 为：" + menuList.stream().map(v -> v.getId()).collect(Collectors.toList()));
//		List<SysMenu> menuList = menuService.listByMenu(new SysMenu());
		List<SysMenu> treeList = menuService.buildMenuTree(menuList);
		List<SysRouter> routerList = menuService.menu2Router(treeList);
		return AjaxResult.success(routerList);
	}

}
