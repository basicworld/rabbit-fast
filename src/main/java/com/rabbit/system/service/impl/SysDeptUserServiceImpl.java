package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.BaseUtils;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptUser;
import com.rabbit.system.domain.SysDeptUserExample;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.mapper.SysDeptUserMapper;
import com.rabbit.system.service.ISysDeptUserService;

@Service
public class SysDeptUserServiceImpl implements ISysDeptUserService {
	@Autowired
	SysDeptUserMapper deptUserMapper;

	@Override
	public Integer insertSelective(SysDeptUser item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		return deptUserMapper.insertSelective(item);
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		return deptUserMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateSelective(SysDeptUser item) {
		item.setUpdateTime(new Date());
		return deptUserMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysDeptUser selectByPrimaryKey(Long id) {
		return deptUserMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysDeptUser> listByDeptId(Long deptId) {
		SysDeptUserExample example = new SysDeptUserExample();
		example.createCriteria().andDeptIdEqualTo(deptId);
		return deptUserMapper.selectByExample(example);

	}

	@Override
	public Integer deleteByUserId(Long userId) {
		SysDeptUserExample example = new SysDeptUserExample();
		example.createCriteria().andIdGreaterThan(new Long(0)).andUserIdEqualTo(userId);
		return deptUserMapper.deleteByExample(example);
	}

	@Override
	public Integer insertByDept(SysDept dept) {
		Long deptId = dept.getId();
		Integer count = 0;
		for (Long userId : dept.getUserIds()) {
			SysDeptUser item = new SysDeptUser();
			item.setDeptId(deptId);
			item.setUserId(userId);
			count += insertSelective(item);
		}
		return count;
	}

	@Override
	public Integer updateByDept(SysDept dept) {
		Long deptId = dept.getId();
		Long[] newUserIds = dept.getUserIds();
		Set<Long> newUserIdSet = Stream.of(newUserIds).collect(Collectors.toSet());

		List<SysDeptUser> existItems = listByDeptId(deptId);
		Set<Long> oldUserIdSet = existItems.stream().map(v -> v.getUserId()).collect(Collectors.toSet());
		// 数据库待新增 用户ID
		Set<Long> userIdToBeInsert = newUserIdSet.stream().filter(v -> !oldUserIdSet.contains(v))
				.collect(Collectors.toSet());
		// 数据库待删除 用户ID
		List<Long> userIdToBeDelete = oldUserIdSet.stream().filter(v -> !newUserIdSet.contains(v))
				.collect(Collectors.toList());

		Integer count = 0;
		// 新增
		for (Long userId : userIdToBeInsert) {
			SysDeptUser item = new SysDeptUser();
			item.setUserId(userId);
			item.setDeptId(deptId);
			count += insertSelective(item);
		}
		// 删除
		SysDeptUserExample example = new SysDeptUserExample();
		example.createCriteria().andDeptIdGreaterThan(new Long(0)).andUserIdIn(userIdToBeDelete)
				.andDeptIdEqualTo(deptId);
		count += deptUserMapper.deleteByExample(example);
		return count;
	}

	@Override
	public Integer insertByUser(SysUser user) {
		SysDeptUser item = new SysDeptUser();
		item.setUserId(user.getId());
		item.setDeptId(user.getDeptId());
		return insertSelective(item);
	}

	@Override
	public Integer updateByUser(SysUser user) {
		deleteByUserId(user.getId());
		return insertByUser(user);
	}

	@Override
	public SysDeptUser selectByUserId(Long userId) {
		SysDeptUserExample example = new SysDeptUserExample();
		example.createCriteria().andUserIdEqualTo(userId);
		return BaseUtils.firstItemOfList(deptUserMapper.selectByExample(example));
	}

}
