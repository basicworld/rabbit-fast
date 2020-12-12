package com.rabbit.system.controller;

import java.util.List;

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

/**
 * 路由controller
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping()
public class SysRouterController {

	private static final Logger logger = LoggerFactory.getLogger(SysRouterController.class);

	@Autowired
	ISysMenuService menuService;
	@Autowired
	TokenService tokenService;

	/**
	 * 获取路由列表
	 * 
	 * @return
	 */
	@GetMapping("/router")
	public AjaxResult getRouters() {
		logger.debug("获取路由...");
		// 根据用户信息获取菜单列表
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUser user = loginUser.getUser();
		List<SysMenu> menuList = menuService.listByUserId(user.getId());
		List<SysMenu> treeList = menuService.buildMenuTree(menuList);
		// 路由树
		List<SysRouter> routerList = menuService.menu2Router(treeList);
		return AjaxResult.success(routerList);
	}

}
