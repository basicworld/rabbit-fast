package com.rabbit.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.sql.SqlUtil;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.system.constant.AccountConstants;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptUser;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.SysUserExample;
import com.rabbit.system.domain.dto.SysUserDTO;
import com.rabbit.system.mapper.SysUserMapper;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysDeptService;
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

	@Autowired
	ISysDeptService deptService;

	@Override
	@Transactional
	public Integer insertSelective(SysUser item) {
		// user表插入
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		Integer count = userMapper.insertSelective(item);
		// account表插入
		for (SysAccount account : item.getAllAccounts()) {
			account.setUserId(item.getId());
			accountService.insertSelective(account);
		}
		// dept关联
		if (StringUtils.isNotNull(item.getDeptId())) {
			deptUserService.insertByUser(item);
		}
		// role关联
		if (StringUtils.isNotNull(item.getRoleIds())) {
			userRoleService.insertByUser(item);
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
	public Integer deleteByPrimaryKey(Long[] userIds) {
		Integer count = 0;
		for (Long id : userIds) {
			// 删除角色关联
			userRoleService.deleteByUserId(id);
			// 删除部门关联
			deptUserService.deleteByUserId(id);
			// 删除account
			accountService.deleteByUserId(id);
			// 物理删除user
			count += userMapper.deleteByPrimaryKey(id);
		}
		return count;
	}

	@Override
	@Transactional
	public Integer updateSelective(SysUser item) {
		item.setUpdateTime(new Date());
		// 账号更新
		if (StringUtils.isNotNull(item.getAllAccounts())) {
			accountService.updateByUser(item);
		}
		// 部门关联
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
		if (StringUtils.isNull(user) || StringUtils.isNull(user.getAllAccounts())
				|| StringUtils.isNull(user.getPassword())) {
			return ValidResult.error("user或user.allAccounts或user.password为空");
		}
		List<SysAccount> allAccounts = user.getAllAccounts();
		if (StringUtils.isEmpty(allAccounts)) {
			return ValidResult.error("用户名不能为空");
		}
		for (SysAccount account : allAccounts) {
			ValidResult result = accountService.validCheckBeforeInsert(account);
			if (result.hasError()) {
				return result;
			}
		}

		return ValidResult.success();
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
		if (StringUtils.isNotEmpty(user.getAllAccounts())) {
			for (SysAccount account : user.getAllAccounts()) {
				ValidResult result = accountService.validCheckBeforeUpdate(account);
				if (result.hasError()) {
					return result;
				}
			}
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

	@Override
	@Transactional
	public Integer deleteByPrimaryKeyLogically(Long[] userIds) {
		Integer count = 0;
		for (Long id : userIds) {
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
			count += updateSelective(user);
		}
		return count;
	}

	@Override
	public SysUser dto2User(SysUserDTO userDTO) {
		SysUser user = new SysUser();
		user.setId(userDTO.getUserId());
		user.setName(userDTO.getNickname());
		user.setPassword(userDTO.getPassword());
		user.setDeleted(userDTO.getDeleted());
		user.setDeptId(userDTO.getDeptId());
		user.setRoleIds(userDTO.getRoleIds());
		List<SysAccount> allAccounts = new ArrayList<SysAccount>();
		// 用户名
		if (StringUtils.isNotNull(userDTO.getUsername())) {
			SysAccount item = new SysAccount();
			item.setUserId(userDTO.getUserId());
			item.setCategory(AccountConstants.CATEGORY_USERNAME);
			item.setOpenCode(userDTO.getUsername());
			allAccounts.add(item);
		}
		// 手机号
		if (StringUtils.isNotNull(userDTO.getPhone())) {
			SysAccount item = new SysAccount();
			item.setUserId(userDTO.getUserId());
			item.setCategory(AccountConstants.CATEGORY_PHONE);
			item.setOpenCode(userDTO.getPhone());
			allAccounts.add(item);
		}
		// 邮箱
		if (StringUtils.isNotNull(userDTO.getEmail())) {
			SysAccount item = new SysAccount();
			item.setUserId(userDTO.getUserId());
			item.setCategory(AccountConstants.CATEGORY_EMAIL);
			item.setOpenCode(userDTO.getEmail());
			allAccounts.add(item);
		}
		// 身份证
		if (StringUtils.isNotNull(userDTO.getIdcard())) {
			SysAccount item = new SysAccount();
			item.setUserId(userDTO.getUserId());
			item.setCategory(AccountConstants.CATEGORY_IDCARD);
			item.setOpenCode(userDTO.getIdcard());
			allAccounts.add(item);
		}
		// 微信号
		if (StringUtils.isNotNull(userDTO.getWechat())) {
			SysAccount item = new SysAccount();
			item.setUserId(userDTO.getUserId());
			item.setCategory(AccountConstants.CATEGORY_WECHAT);
			item.setOpenCode(userDTO.getWechat());
			allAccounts.add(item);
		}
		user.setAllAccounts(allAccounts);
		return user;
	}

	@Override
	public SysUserDTO user2dto(SysUser user) {
		SysUserDTO dto = new SysUserDTO();
		dto.setUserId(user.getId());
		dto.setNickname(user.getName());
		dto.setPassword("******");
		dto.setDeleted(user.getDeleted());
		dto.setRoleIds(user.getRoleIds());
		SysDeptUser deptUser = deptUserService.selectByUserId(user.getId());
		// 补全部门信息
		if (StringUtils.isNotNull(deptUser)) {
			Long deptId = deptUser.getDeptId();
			dto.setDeptId(deptId);
			SysDept dept = deptService.selectByPrimaryKey(deptId);
			dto.setDeptName(StringUtils.isNotNull(dept) ? dept.getName() : "");
		}
		// 补全账号信息
		List<SysAccount> allAccounts = user.getAllAccounts();
		if (StringUtils.isNotEmpty(allAccounts)) {
			for (SysAccount account : allAccounts) {
				if (AccountConstants.CATEGORY_USERNAME == account.getCategory()) {
					dto.setUsername(account.getOpenCode());
				} else if (AccountConstants.CATEGORY_PHONE == account.getCategory()) {
					dto.setPhone(account.getOpenCode());
				} else if (AccountConstants.CATEGORY_EMAIL == account.getCategory()) {
					dto.setEmail(account.getOpenCode());
				} else if (AccountConstants.CATEGORY_IDCARD == account.getCategory()) {
					dto.setIdcard(account.getOpenCode());
				} else if (AccountConstants.CATEGORY_WECHAT == account.getCategory()) {
					dto.setWechat(account.getOpenCode());
				}
			}
		}
		return dto;
	}

	@Override
	public List<SysUser> listByUserDTO(SysUserDTO userDTO) {
		SysUserExample example = new SysUserExample();
		SysUserExample.Criteria c1 = example.createCriteria();
		if (StringUtils.isNull(userDTO.getDeleted()) || false == userDTO.getDeleted()) {
			c1.andDeletedEqualTo(false);
		} else {
			c1.andDeletedEqualTo(true);
		}
		if (StringUtils.isNotNull(userDTO.getNickname())) {
			c1.andNameLike(SqlUtil.getFuzzQueryParam(userDTO.getNickname()));
		}
		return userMapper.selectByExample(example);
	}

}
