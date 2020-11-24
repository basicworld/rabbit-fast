package com.rabbit.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.mapper.SysUserMapper;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysDeptUserService;
import com.rabbit.system.service.ISysUserRoleService;
import com.rabbit.system.service.ISysUserService;

@Service
public class SysUserServiceImpl implements ISysUserService {
	@Autowired
	SysUserMapper userMapper;

	@Autowired
	ISysAccountService accountService;

	@Autowired
	ISysUserRoleService userRoleService;

	@Autowired
	ISysDeptUserService deptUserService;

	@Override
	@Transactional
	public Integer insertSelective(SysUser item) {
		// user表插入
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		Integer count = userMapper.insertSelective(item);
		// account表插入
		SysAccount account = item.getAccount();
		account.setUserId(item.getId());
		accountService.insertSelective(account);
		// dept关联
		if (StringUtils.isNotNull(item.getDeptId())) {
			deptUserService.insertByUser(item);
		}
		return count;
	}

	@Override
	@Transactional
	public Integer deleteByPrimaryKey(Long id) {
		// 删除角色关联
		userRoleService.deleteByUserId(id);
		// 删除部门关联
		deptUserService.deleteByUserId(id);
		// 删除account
		accountService.deleteByUserId(id);
		// 物理删除user
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public Integer updateSelective(SysUser item) {
		item.setUpdateTime(new Date());
		// 账号更新
		SysAccount account = item.getAccount();
		if (StringUtils.isNotNull(account)) {
			accountService.insertOrUpdateSelective(account);
		}
		// dept关联
		if (StringUtils.isNotNull(item.getDeptId())) {
			deptUserService.updateByUser(item);
		}
		// 角色更新
		if (StringUtils.isNotNull(item.getRoleIds())) {
			userRoleService.updateByUser(item);
		}
		return userMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysUser selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysUser> account2User(List<SysAccount> accountList) {
		List<SysUser> userList = new ArrayList<SysUser>();
		for (SysAccount account : accountList) {
			SysUser user = account2User(account);
			if (StringUtils.isNotNull(user)) {
				userList.add(user);
			}
		}
		return userList;
	}

	@Override
	public SysUser account2User(SysAccount account) {
		SysUser user = userMapper.selectByPrimaryKey(account.getUserId());
		user.setAccount(account);
		return user;
	}

	@Override
	public ValidResult validCheckBeforeInsert(SysUser user) {
		if (StringUtils.isNull(user) || StringUtils.isNull(user.getAccount())
				|| StringUtils.isNull(user.getPassword())) {
			return ValidResult.error("user或user.account或user.password为空");
		}
		SysAccount account = user.getAccount();
		return accountService.validCheckBeforeInsert(account);
	}

	@Override
	public ValidResult validCheckBeforeUpdate(SysUser user) {
		if (StringUtils.isNull(user) || StringUtils.isNull(user.getId())) {
			return ValidResult.error("用户或用户ID为空");
		}
		SysUser existUser = selectByPrimaryKey(user.getId());
		if (StringUtils.isNull(existUser) || true == existUser.getDeleted()) {
			return ValidResult.error("用户不存在");
		}
		// account 校验
		SysAccount account = user.getAccount();
		if (StringUtils.isNotNull(account)) {
			return accountService.validCheckBeforeUpdate(user.getAccount());
		}
		return ValidResult.success();
	}

	@Override
	public ValidResult validCheckBeforeDelete(Long userId) {
		if (StringUtils.isNull(userId)) {
			return ValidResult.error("userId为空");
		}
		SysUser existUser = selectByPrimaryKey(userId);
		List<SysAccount> existAccountList = accountService.listByUserId(userId);
		if (StringUtils.isNull(existUser) && existAccountList.size() < 1) {
			return ValidResult.error("不存在的userId");
		}
		return ValidResult.success();
	}

	@Override
	public Integer deleteByPrimaryKeyLogically(Long id) {
		// 删除角色关联
		userRoleService.deleteByUserId(id);
		// 删除部门关联
		deptUserService.deleteByUserId(id);
		// 删除account
		accountService.deleteByUserId(id);
		SysUser user = new SysUser();
		user.setId(id);
		user.setDeleted(true);
		// 逻辑删除user
		return updateSelective(user);
	}

}
