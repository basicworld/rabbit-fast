package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.StringUtils;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptRole;
import com.rabbit.system.domain.SysDeptRoleExample;
import com.rabbit.system.mapper.SysDeptRoleMapper;
import com.rabbit.system.service.ISysDeptRoleService;

@Service
public class SysDeptRoleServiceImpl implements ISysDeptRoleService {
	@Autowired
	SysDeptRoleMapper deptRoleMapper;

	@Override
	public Integer insertSelective(SysDeptRole item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		return deptRoleMapper.insertSelective(item);
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		return deptRoleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateSelective(SysDeptRole item) {
		item.setUpdateTime(new Date());
		return deptRoleMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysDeptRole selectByPrimaryKey(Long id) {
		return deptRoleMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysDeptRole> listByDeptId(Long deptId) {
		SysDeptRoleExample example = new SysDeptRoleExample();
		example.createCriteria().andDeptIdEqualTo(deptId);
		return deptRoleMapper.selectByExample(example);
	}

	@Override
	public List<SysDeptRole> listByRoleId(Long roleId) {
		SysDeptRoleExample example = new SysDeptRoleExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		return deptRoleMapper.selectByExample(example);
	}

	@Override
	public Integer insertIfNotExists(SysDeptRole deptRole) {
		SysDeptRoleExample example = new SysDeptRoleExample();
		example.createCriteria().andDeptIdEqualTo(deptRole.getDeptId()).andRoleIdEqualTo(deptRole.getRoleId());
		List<SysDeptRole> existItems = deptRoleMapper.selectByExample(example);
		if (existItems.size() == 0) {
			return insertSelective(deptRole);
		}
		if (existItems.size() == 1) {
			return 0; // 无需更新
		}
		if (existItems.size() > 1) {
			deptRoleMapper.deleteByExample(example);
			return insertSelective(deptRole);
		}
		return 0;
	}

	@Override
	public Integer insertByDept(SysDept dept) {
		Long deptId = dept.getId();
		Integer count = 0;
		for (Long roleId : dept.getRoleIds()) {
			SysDeptRole item = new SysDeptRole();
			item.setDeptId(deptId);
			item.setRoleId(roleId);
			count += insertSelective(item);
		}
		return count;
	}

	/**
	 * 更新部门-角色关系
	 */
	@Override
	public Integer updateByDept(SysDept dept) {
		Long deptId = dept.getId();
		Long[] newRoleIds = dept.getRoleIds();
		Integer count = 0;
		// 所有新角色ID
		Set<Long> newRoleIdSet = Stream.of(newRoleIds).collect(Collectors.toSet());

		List<SysDeptRole> existItems = listByDeptId(deptId);
		// 所有已存在角色ID
		Set<Long> oldRoleIdSet = existItems.stream().map(v -> v.getRoleId()).collect(Collectors.toSet());
		// 数据库待新增的角色ID
		Set<Long> roleIdsToBeInsert = newRoleIdSet.stream().filter(v -> !oldRoleIdSet.contains(v))
				.collect(Collectors.toSet());
		// 数据库待删除的角色ID
		List<Long> roleIdsToBeDelete = oldRoleIdSet.stream().filter(v -> !newRoleIdSet.contains(v))
				.collect(Collectors.toList());
		// 新增
		for (Long roleId : roleIdsToBeInsert) {
			SysDeptRole item = new SysDeptRole();
			item.setRoleId(roleId);
			item.setDeptId(deptId);
			count += insertSelective(item);
		}
		// 删除
		if (StringUtils.isNotEmpty(roleIdsToBeDelete)) {
			SysDeptRoleExample example = new SysDeptRoleExample();
			example.createCriteria().andIdGreaterThan(new Long(0)).andRoleIdIn(roleIdsToBeDelete)
					.andDeptIdEqualTo(deptId);
			count += deptRoleMapper.deleteByExample(example);
		}
		return count;
	}

	@Override
	public Integer deleteByDeptId(Long deptId) {
		SysDeptRoleExample example = new SysDeptRoleExample();
		example.createCriteria().andIdGreaterThan(new Long(0)).andDeptIdEqualTo(deptId);
		return deptRoleMapper.deleteByExample(example);
	}

}
