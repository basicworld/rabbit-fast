package com.rabbit.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.StringUtils;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysRoleMenu;
import com.rabbit.system.domain.SysRoleMenuExample;
import com.rabbit.system.mapper.SysRoleMenuMapper;
import com.rabbit.system.service.ISysRoleMenuService;

@Service
public class SysRoleMenuServiceImpl implements ISysRoleMenuService {
	@Autowired
	SysRoleMenuMapper roleMenuMapper;

	@Override
	public Integer insertSelective(SysRoleMenu item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		return roleMenuMapper.insertSelective(item);
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		return roleMenuMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateSelective(SysRoleMenu item) {
		item.setUpdateTime(new Date());
		return roleMenuMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysRoleMenu selectByPrimaryKey(Long id) {
		return roleMenuMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysRoleMenu> listByRoleId(Long roleId) {
		SysRoleMenuExample example = new SysRoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		return roleMenuMapper.selectByExample(example);
	}

	@Override
	public List<SysRoleMenu> listByRoleId(Long[] roleIds) {
		if (StringUtils.isEmpty(roleIds)) {
			return new ArrayList<SysRoleMenu>();
		}

		List<Long> uniqueRoleIds = Stream.of(roleIds).collect(Collectors.toSet()).stream().collect(Collectors.toList());
		SysRoleMenuExample example = new SysRoleMenuExample();
		example.createCriteria().andRoleIdIn(uniqueRoleIds);
		return roleMenuMapper.selectByExample(example);

	}

	@Override
	public List<SysRoleMenu> listByMenuId(Long menuId) {
		SysRoleMenuExample example = new SysRoleMenuExample();
		example.createCriteria().andMenuIdEqualTo(menuId);
		return roleMenuMapper.selectByExample(example);
	}

	@Override
	public Integer deleteByRoleId(Long roleId) {
		SysRoleMenuExample example = new SysRoleMenuExample();
		example.createCriteria().andIdGreaterThan(new Long(0)).andRoleIdEqualTo(roleId);
		return roleMenuMapper.deleteByExample(example);
	}

	@Override
	public Integer updateByRole(SysRole role) {
		Long roleId = role.getId();
		Long[] menuIds = role.getMenuIds();
		Set<Long> newMenuIdSet = Stream.of(menuIds).collect(Collectors.toSet());

		List<SysRoleMenu> existItems = listByRoleId(roleId);
		Set<Long> oldMenuIdSet = existItems.stream().map(v -> v.getMenuId()).collect(Collectors.toSet());

		// 数据库待新增 菜单ID
		Set<Long> menuIdToBeInsert = newMenuIdSet.stream().filter(v -> !oldMenuIdSet.contains(v))
				.collect(Collectors.toSet());

		// 数据库待删除 菜单ID
		List<Long> menuIdToBeDelete = oldMenuIdSet.stream().filter(v -> !newMenuIdSet.contains(v))
				.collect(Collectors.toList());
		Integer count = 0;
		// 新增
		for (Long menuId : menuIdToBeInsert) {
			SysRoleMenu item = new SysRoleMenu();
			item.setMenuId(menuId);
			item.setRoleId(roleId);
			count += insertSelective(item);
		}

		// 删除
		if (StringUtils.isNotEmpty(menuIdToBeDelete)) {
			SysRoleMenuExample example = new SysRoleMenuExample();
			example.createCriteria().andIdGreaterThan(new Long(0)).andMenuIdIn(menuIdToBeDelete)
					.andRoleIdEqualTo(roleId);
			count += roleMenuMapper.deleteByExample(example);

		}
		return count;
	}

	@Override
	public Integer insertByRole(SysRole role) {
		Long roleId = role.getId();
		Integer count = 0;
		for (Long menuId : role.getMenuIds()) {
			SysRoleMenu item = new SysRoleMenu();
			item.setRoleId(roleId);
			item.setMenuId(menuId);
			count += insertSelective(item);
		}
		return count;
	}

}
