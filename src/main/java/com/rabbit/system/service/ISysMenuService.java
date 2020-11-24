package com.rabbit.system.service;

import java.util.List;

import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysMenu;

public interface ISysMenuService extends BaseService<SysMenu> {
	/**
	 * 查询菜单列表
	 * 
	 * @param menu
	 * @return
	 */
	List<SysMenu> listByMenu(SysMenu menu);

	/**
	 * 查询菜单树
	 * 
	 * @param menuList
	 * @return
	 */
	List<SysMenu> buildMenuTree(List<SysMenu> menuList);

	/**
	 * 查询菜单下拉树，前端下拉选择用
	 * 
	 * @param menuList
	 * @return
	 */
	List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menuList);

}
