package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.sql.SqlUtil;
import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.constant.DeptConstants;
import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.domain.SysMenuExample;
import com.rabbit.system.mapper.SysMenuMapper;
import com.rabbit.system.service.ISysMenuService;

@Service
public class SysMenuServiceImpl implements ISysMenuService {
	@Autowired
	SysMenuMapper menuMapper;

	@Override
	public Integer insertSelective(SysMenu item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		return menuMapper.insertSelective(item);
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return menuMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateSelective(SysMenu item) {
		item.setUpdateTime(new Date());
		return menuMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysMenu selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return menuMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysMenu> listByMenu(SysMenu menu) {
		SysMenuExample example = new SysMenuExample();
		SysMenuExample.Criteria c1 = example.createCriteria();
		if (StringUtils.isNotNull(menu)) {
			if (StringUtils.isNull(menu.getDeleted()) || false == menu.getDeleted()) {
				c1.andDeletedEqualTo(false);
			}
			if (StringUtils.isNotNull(menu.getName())) {
				c1.andPathLike(SqlUtil.getFuzzQueryParam(menu.getPath()));
			}
		}

		return menuMapper.selectByExample(example);
	}

	@Override
	public List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
		Map<Long, List<SysMenu>> menuByParentIdMap = menuList.stream()
				.collect(Collectors.groupingBy(SysMenu::getParentId));
		menuList.forEach(dept -> dept.setChildren(menuByParentIdMap.get(dept.getId())));
		return menuList.stream().filter(v -> v.getParentId().equals(DeptConstants.ROOT_DEPT_ID))
				.collect(Collectors.toList());
	}

	@Override
	public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menuList) {
		List<SysMenu> menuTrees = buildMenuTree(menuList);
		return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
	}

}
