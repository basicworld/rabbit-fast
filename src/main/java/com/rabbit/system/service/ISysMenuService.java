package com.rabbit.system.service;

import java.util.List;

import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.domain.dto.SysRouter;

/**
 * 菜单service
 * 
 * @author wlfei
 *
 */
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

	/**
	 * menu转为路由，前端侧边栏路由展示用
	 * 
	 * @param menu
	 * @return
	 */
	SysRouter menu2Router(SysMenu menu);

	/**
	 * menu转为路由，前端侧边栏路由展示用
	 * 
	 * @param menuList
	 * @return
	 */
	List<SysRouter> menu2Router(List<SysMenu> menuList);

	/**
	 * 根据主键批量获取
	 * 
	 * @param menuIds
	 * @return
	 */
	List<SysMenu> listByPrimaryKeys(Long[] menuIds);

	/**
	 * 根据用户ID查询菜单<br>
	 * 如果用户是超级管理员，则返回所有菜单
	 * 
	 * @param userId
	 * @return
	 */
	List<SysMenu> listByUserId(Long userId);

	/**
	 * 根据角色主键查询
	 * 
	 * @param userId
	 * @return
	 */
	List<SysMenu> listByRoleId(Long roleId);

	/**
	 * 根据角色主键查询
	 * 
	 * @param userId
	 * @return
	 */
	List<SysMenu> listByRoleId(Long[] roleIds);

}
