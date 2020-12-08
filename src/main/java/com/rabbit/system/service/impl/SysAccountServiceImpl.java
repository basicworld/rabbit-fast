package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.sql.SqlUtil;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.common.util.valid.ValidUtils;
import com.rabbit.system.constant.AccountConstants;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysAccountExample;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;
import com.rabbit.system.mapper.SysAccountMapper;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysUserService;

@Service
public class SysAccountServiceImpl implements ISysAccountService {
	@Autowired
	SysAccountMapper accountMapper;

	@Autowired
	ISysUserService userService;

	@Override
	public Integer insertSelective(SysAccount item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		return accountMapper.insertSelective(item);
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		return accountMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public Integer deleteByPrimaryKey(Long[] accountIds) {
		Integer count = 0;
		for (Long accountId : accountIds) {
			// 删除账号
			count += accountMapper.deleteByPrimaryKey(accountId);

		}
		return count;
	}

	@Override
	public Integer updateSelective(SysAccount item) {
		item.setUpdateTime(new Date());
		return accountMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysAccount selectByPrimaryKey(Long id) {
		return accountMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysAccount> listByUserId(Long userId) {
		SysAccountExample example = new SysAccountExample();
		example.createCriteria().andUserIdEqualTo(userId);
		return accountMapper.selectByExample(example);
	}

	@Override
	public List<SysAccount> listByUserDTO(SysUserDTO userDTO) {
		SysAccountExample example = new SysAccountExample();
		SysAccountExample.Criteria c1 = example.createCriteria();
		// 构造查询条件
		if (StringUtils.isNotNull(userDTO)) {
			// 设置删除标记
			if (StringUtils.isNull(userDTO.getDeleted()) || false == userDTO.getDeleted()) {
				c1.andDeletedEqualTo(false);
			} else {
				c1.andDeletedEqualTo(true);
			}
			// 设置账号类型
			if (StringUtils.isNotNull(userDTO.getCategory())) {
				c1.andCategoryEqualTo(userDTO.getCategory());
			}
			// 设置账号名
			if (StringUtils.isNotNull(userDTO.getUsername())) {
				c1.andOpenCodeLike(SqlUtil.getFuzzQueryParam(userDTO.getUsername()));
			}
		}
		return accountMapper.selectByExample(example);
	}

	@Override
	public List<SysAccount> listByCategoryAndOpenCode(SysAccount account) {
		SysAccountExample example = new SysAccountExample();
		example.createCriteria().andCategoryEqualTo(account.getCategory()).andOpenCodeEqualTo(account.getOpenCode());

		return accountMapper.selectByExample(example);
	}

	@Override
	public Integer deleteByUserId(Long userId) {
		SysAccountExample example = new SysAccountExample();
		example.createCriteria().andIdGreaterThan(new Long(0)).andUserIdEqualTo(userId);
		return accountMapper.deleteByExample(example);
	}

	@Override
	public Integer deleteByUserIdAndCategory(SysAccount account) {
		SysAccountExample example = new SysAccountExample();
		example.createCriteria().andIdGreaterThan(new Long(0)).andUserIdEqualTo(account.getUserId())
				.andCategoryEqualTo(account.getCategory());
		return accountMapper.deleteByExample(example);
	}

	@Override
	public List<SysAccount> listByCategoryAndUserId(SysAccount account) {
		SysAccountExample example = new SysAccountExample();
		example.createCriteria().andCategoryEqualTo(account.getCategory()).andUserIdEqualTo(account.getUserId());
		return accountMapper.selectByExample(example);
	}

	@Override
	public ValidResult validCheckBeforeInsert(SysAccount account) {
		// 格式校验
		ValidResult preValid = validCheckBeforeInsertOrUpdate(account);
		if (preValid.hasError()) {
			return preValid;
		}
		// 重复账号判断：账号名+账号类型重复
		List<SysAccount> duplicateAccountList = listByCategoryAndOpenCode(account);
		if (duplicateAccountList.size() > 0) {
			return ValidResult.error("账号重复:" + account.getOpenCode());
		}
		return ValidResult.success();

	}

	/**
	 * 符合各类型账号的基本格式，不允许有账号重复
	 */
	@Override
	public ValidResult validCheckBeforeUpdate(SysAccount account) {
		// 基本格式校验
		ValidResult preValid = validCheckBeforeInsertOrUpdate(account);
		if (preValid.hasError()) {
			return preValid;
		}
		// 重复账号判断：其他用户有账号类型+账号名相同的账号，视为重复
		if (StringUtils.isNotNull(account.getUserId()) && StringUtils.isNotNull(account.getCategory())
				&& StringUtils.isNotNull(account.getOpenCode())) {
			SysAccountExample example = new SysAccountExample();

			example.createCriteria().andUserIdNotEqualTo(account.getUserId()).andCategoryEqualTo(account.getCategory())
					.andOpenCodeEqualTo(account.getOpenCode());
			List<SysAccount> duplicateAccountList = accountMapper.selectByExample(example);
			if (duplicateAccountList.size() > 0) {
				return ValidResult.error("账号重复:" + account.getOpenCode());
			}
		}
		return ValidResult.success();
	}

	@Override
	public ValidResult validCheckBeforeDelete(Long accountId) {
		if (StringUtils.isNull(accountId)) {
			return ValidResult.error("账户类型为空");
		}
		return ValidResult.success();
	}

	/**
	 * 账号格式校验
	 */
	@Override
	public ValidResult validCheckBeforeInsertOrUpdate(SysAccount account) {
		if (StringUtils.isNull(account) || StringUtils.isNull(account.getCategory())
				|| StringUtils.isNull(account.getOpenCode())) {
			return ValidResult.error("账号、账号类型不能为空");
		}
		if (!AccountConstants.VALID_CATEGORY_SET.contains(account.getCategory())) {
			return ValidResult.error("账号类型不存在");
		}
		if (AccountConstants.CATEGORY_EMAIL == account.getCategory()) {
			if (!ValidUtils.isValidEmail(account.getOpenCode())) {
				return ValidResult.error("邮箱格式异常");
			}
		}
		if (AccountConstants.CATEGORY_PHONE == account.getCategory()) {
			if (!ValidUtils.isValidPhone(account.getOpenCode())) {
				return ValidResult.error("手机号格式异常");
			}
		}
		if (AccountConstants.CATEGORY_USERNAME == account.getCategory()) {
			// 全小写字符和数字、字母开头、长度限制为5-30字符
			String username = account.getOpenCode();
			boolean formatCheckOk = ValidUtils.isLengthBetween(username, 5, 31)
					&& ValidUtils.isAllNumberOrSmallcaseAlphaBet(username)
					&& ValidUtils.isStartWithSmallcaseAlphaBet(username);
			if (!formatCheckOk) {
				return ValidResult.error("用户名格式异常，请核对正确格式：5-30长度、全小写字母或数字、字母开头");
			}
		}
		if (AccountConstants.CATEGORY_IDCARD == account.getCategory()) {
			String idcard = account.getOpenCode();
			if (!ValidUtils.isLengthBetween(idcard, 18, 19)) {
				return ValidResult.error("身份证长度不符合要求");
			}
		}
		if (AccountConstants.CATEGORY_WECHAT == account.getCategory()) {
			String wechat = account.getOpenCode();
			if (StringUtils.isBlank(wechat)) {
				return ValidResult.error("微信号格式不符合要求");
			}
		}

		return ValidResult.success();
	}

	/**
	 * 为用户更新账号
	 */
	@Override
	public Integer updateByUser(SysUser user) {
		List<SysAccount> allAccounts = user.getAllAccounts();
		Long userId = user.getId();
		Integer count = 0;
		// 删除用户所有账号
		deleteByUserId(userId);
		// 新增账号
		for (SysAccount account : allAccounts) {
			count += insertSelective(account);
		}
		return count;
	}

}
