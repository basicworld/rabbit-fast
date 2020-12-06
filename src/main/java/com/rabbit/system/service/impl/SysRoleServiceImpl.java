package com.rabbit.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.sql.SqlUtil;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.system.constant.RoleConstants;
import com.rabbit.system.domain.SysDeptUser;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysRoleExample;
import com.rabbit.system.mapper.SysRoleMapper;
import com.rabbit.system.service.ISysDeptRoleService;
import com.rabbit.system.service.ISysDeptUserService;
import com.rabbit.system.service.ISysRoleMenuService;
import com.rabbit.system.service.ISysRoleService;
import com.rabbit.system.service.ISysUserRoleService;

@Service
public class SysRoleServiceImpl implements ISysRoleService {
	@Autowired
	SysRoleMapper roleMapper;

	@Autowired
	ISysRoleMenuService roleMenuService;
	@Autowired
	ISysDeptUserService deptUserService;

	@Autowired
	ISysUserRoleService userRoleService;

	@Autowired
	ISysDeptRoleService deptRoleService;

	@Override
	public Integer insertSelective(SysRole item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		Integer count = roleMapper.insertSelective(item);
		if (StringUtils.isNotNull(item.getMenuIds())) {
			roleMenuService.insertByRole(item);
		}

		return count;
	}

	@Override
	@Transactional
	public Integer deleteByPrimaryKey(Long id) {
		roleMenuService.deleteByRoleId(id);
		return roleMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public Integer deleteByPrimaryKey(Long[] roleIds) {
		Integer count = 0;
		for (Long roleId : roleIds) {
			roleMenuService.deleteByRoleId(roleId);
			count += roleMapper.deleteByPrimaryKey(roleId);
		}
		return count;
	}

	@Override
	@Transactional
	public Integer updateSelective(SysRole item) {
		if (StringUtils.isNotNull(item)) {
			roleMenuService.updateByRole(item);
		}
		item.setUpdateTime(new Date());
		return roleMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysRole selectByPrimaryKey(Long id) {
		return roleMapper.selectByPrimaryKey(id);
	}

	@Override
	public ValidResult validCheckBeforeInsert(SysRole role) {
		if (RoleConstants.ADMIN_ROLE_CODE.equals(role.getCode())
				|| RoleConstants.ADMIN_ROLE_NAME.equals(role.getName())) {
			return ValidResult.error("角色代码或角色名称不能与超级管理员重复");
		}
		// 重复name或code判断
		int codeDuplicateCount = listByCodeEqualsTo(role.getCode()).size();
		int nameDuplicateCount = listByNameEqualsTo(role.getName()).size();
		if (codeDuplicateCount + nameDuplicateCount > 0) {
			return ValidResult.error("角色代码或角色名称重复");
		}

		return ValidResult.success();
	}

	@Override
	public ValidResult validCheckBeforeUpdate(SysRole role) {
		if (StringUtils.isNull(role) || StringUtils.isNull(role.getId())) {
			return ValidResult.error("缺少关键参数：role 或 role.id为空");
		}
		if (RoleConstants.ADMIN_ROLE_ID.equals(role.getId())) {
			return ValidResult.error("不允许修改超级管理员配置");
		}
		// 重复code或name判断
		if (RoleConstants.ADMIN_ROLE_CODE.equals(role.getCode())
				|| RoleConstants.ADMIN_ROLE_NAME.equals(role.getName())) {
			return ValidResult.error("角色代码或角色名称不能与超级管理员重复");
		}

		List<SysRole> roleListWithSameCode = listByCodeEqualsTo(role.getCode());
		int codeDuplicateCount = roleListWithSameCode.stream().filter(v -> !role.getId().equals(v.getId()))
				.collect(Collectors.toList()).size();
		List<SysRole> roleListWithSameName = listByNameEqualsTo(role.getName());
		int nameDuplicateCount = roleListWithSameName.stream().filter(v -> !role.getId().equals(v.getId()))
				.collect(Collectors.toList()).size();
		if (codeDuplicateCount + nameDuplicateCount > 0) {
			return ValidResult.error("角色代码或角色名称重复");
		}
		return ValidResult.success();
	}

	@Override
	public ValidResult validCheckBeforeDelete(Long roleId) {
		if (StringUtils.isNull(roleId)) {
			return ValidResult.error("角色ID为空");
		}
		if (RoleConstants.ADMIN_ROLE_ID.equals(roleId)) {
			return ValidResult.error("不允许删除超级管理员");
		}
		SysRole role = selectByPrimaryKey(roleId);
		if (StringUtils.isNull(role) || true == role.getDeleted()) {
			return ValidResult.error("角色不存在");
		}
		if (RoleConstants.ADMIN_ROLE_CODE.equals(role.getCode())
				|| RoleConstants.ADMIN_ROLE_NAME.equals(role.getName())) {
			return ValidResult.error("超级管理员不允许删除");
		}
		// 关联用户判断
		int userRoleCount = userRoleService.listByRoleId(roleId).size();
		if (userRoleCount > 0) {
			return ValidResult.error("角色被用户使用中，不能删除");
		}
		// 关联用户组判断
		int deptRoleCount = deptRoleService.listByRoleId(roleId).size();
		if (deptRoleCount > 0) {
			return ValidResult.error("角色被部门使用中，不能删除");
		}
		return ValidResult.success();
	}

	@Override
	public List<SysRole> listByNameEqualsTo(String name) {
		SysRoleExample example = new SysRoleExample();
		example.createCriteria().andNameEqualTo(name);
		return roleMapper.selectByExample(example);
	}

	@Override
	public List<SysRole> listByCodeEqualsTo(String code) {
		SysRoleExample example = new SysRoleExample();
		example.createCriteria().andCodeEqualTo(code);
		return roleMapper.selectByExample(example);
	}

	@Override
	public List<SysRole> listByRole(SysRole role) {
		SysRoleExample example = new SysRoleExample();
		SysRoleExample.Criteria c1 = example.createCriteria();
		if (StringUtils.isNotNull(role)) {
			if (StringUtils.isNull(role.getDeleted()) || false == role.getDeleted()) {
				c1.andDeletedEqualTo(false);
			} else {
				c1.andDeletedEqualTo(true);
			}
			if (StringUtils.isNotNull(role.getCode())) {
				c1.andCodeLike(SqlUtil.getFuzzQueryParam(role.getCode()));
			}
			if (StringUtils.isNotNull(role.getName())) {
				c1.andNameLike(SqlUtil.getFuzzQueryParam(role.getName()));
			}
		}
		return roleMapper.selectByExample(example);
	}

	@Override
	public List<SysRole> listByPrimaryKeys(Long[] roleIds) {
		List<SysRole> roles = new ArrayList<SysRole>();
		if (StringUtils.isEmpty(roleIds)) {
			return roles;
		}
		Set<Long> uniqueRoleIds = Stream.of(roleIds).collect(Collectors.toSet());
		for (Long roleId : uniqueRoleIds) {
			SysRole role = selectByPrimaryKey(roleId);
			if (StringUtils.isNotNull(role)) {
				roles.add(role);
			}
		}
		return roles;
	}

	@Override
	public List<SysRole> listByUserId(Long userId) {
		// 来自用户的角色ID
		Long[] roleIds = userRoleService.listByUserId(userId).stream().map(v -> v.getRoleId()).toArray(Long[]::new);

		// 来自所属部门的角色ID
		Long[] roleIds2 = new Long[0];
		// 获取部门关联
		SysDeptUser deptUser = deptUserService.selectByUserId(userId);
		if (StringUtils.isNotNull(deptUser)) {
			roleIds2 = deptRoleService.listByDeptId(deptUser.getDeptId()).stream().map(v -> v.getRoleId())
					.toArray(Long[]::new);
		}

		Long[] allRoleIds = (Long[]) ArrayUtils.addAll(roleIds, roleIds2);
		List<SysRole> roles = listByPrimaryKeys(allRoleIds);

		return roles;
	}

}
