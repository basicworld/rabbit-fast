package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.SysUserRole;
import com.rabbit.system.domain.SysUserRoleExample;
import com.rabbit.system.mapper.SysUserRoleMapper;
import com.rabbit.system.service.ISysUserRoleService;

@Service
public class SysUserRoleServiceImpl implements ISysUserRoleService {
	@Autowired
	SysUserRoleMapper userRoleMapper;

	@Override
	public Integer insertSelective(SysUserRole item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		return userRoleMapper.insertSelective(item);
	}

	@Override
	public Integer insertByUser(SysUser user) {
		Long userId = user.getId();
		Integer count = 0;
		for (Long roleId : user.getRoleIds()) {
			SysUserRole item = new SysUserRole();
			item.setUserId(userId);
			item.setRoleId(roleId);
			insertSelective(item);
		}

		return count;
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return userRoleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateSelective(SysUserRole item) {
		item.setUpdateTime(new Date());
		return userRoleMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysUserRole selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return userRoleMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysUserRole> listByUserId(Long userId) {
		SysUserRoleExample example = new SysUserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId);
		return userRoleMapper.selectByExample(example);
	}

	@Override
	public List<SysUserRole> listByRoleId(Long roleId) {
		SysUserRoleExample example = new SysUserRoleExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		return userRoleMapper.selectByExample(example);
	}

	@Override
	public Integer deleteByUserId(Long userId) {
		SysUserRoleExample example = new SysUserRoleExample();
		example.createCriteria().andIdGreaterThan(new Long(0)).andUserIdEqualTo(userId);
		return userRoleMapper.deleteByExample(example);
	}

	@Override
	public Integer updateByUser(SysUser user) {
		Long[] newRoleIds = user.getRoleIds();
		Set<Long> newRoleIdSet = Stream.of(newRoleIds).collect(Collectors.toSet());

		List<SysUserRole> existItems = listByUserId(user.getId());
		Set<Long> oldRoleIdSet = existItems.stream().map(v -> v.getRoleId()).collect(Collectors.toSet());

		Set<Long> roleIdToBeInsert = newRoleIdSet.stream().filter(v -> !oldRoleIdSet.contains(v))
				.collect(Collectors.toSet());
		Set<Long> roleIdToBeDelete = oldRoleIdSet.stream().filter(v -> !newRoleIdSet.contains(v))
				.collect(Collectors.toSet());
		Integer count = 0;
		for (Long roleId : roleIdToBeInsert) {
			SysUserRole item = new SysUserRole();
			item.setRoleId(roleId);
			item.setUserId(user.getId());
			count += insertSelective(item);
		}
		for (Long roleId : roleIdToBeDelete) {
			SysUserRoleExample example = new SysUserRoleExample();
			example.createCriteria().andIdGreaterThan(new Long(0)).andRoleIdEqualTo(roleId)
					.andUserIdEqualTo(user.getId());
			count += userRoleMapper.deleteByExample(example);
		}
		return count;
	}

}
