package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.sql.SqlUtil;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.common.util.valid.ValidUtils;
import com.rabbit.system.constant.AccountConstants;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysAccountExample;
import com.rabbit.system.domain.dto.SysUserDTO;
import com.rabbit.system.mapper.SysAccountMapper;
import com.rabbit.system.service.ISysAccountService;

@Service
public class SysAccountServiceImpl implements ISysAccountService {
	@Autowired
	SysAccountMapper accountMapper;

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
		if (StringUtils.isNotNull(userDTO)) {
			if (StringUtils.isNull(userDTO.getDeleted()) || false == userDTO.getDeleted()) {
				c1.andDeletedEqualTo(false);
			} else {
				c1.andDeletedEqualTo(true);
			}
			if (StringUtils.isNotNull(userDTO.getCategory())) {
				c1.andCategoryEqualTo(userDTO.getCategory());
			}
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
		example.createCriteria().andDeletedEqualTo(false).andIdGreaterThan(new Long(0)).andUserIdEqualTo(userId);
		return accountMapper.deleteByExample(example);
	}

	@Override
	public Integer deleteByUserIdAndCategory(SysAccount account) {
		SysAccountExample example = new SysAccountExample();
		example.createCriteria().andDeletedEqualTo(false).andIdGreaterThan(new Long(0))
				.andUserIdEqualTo(account.getUserId()).andCategoryEqualTo(account.getCategory());
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
		ValidResult preValid = validCheckBeforeInsertOrUpdate(account);
		if (preValid.hasError()) {
			return preValid;
		}
		List<SysAccount> duplicateAccountList = listByCategoryAndOpenCode(account);
		if (duplicateAccountList.size() > 0) {
			return ValidResult.error("账号重复");
		}
		return ValidResult.success();

	}

	@Override
	public ValidResult validCheckBeforeUpdate(SysAccount account) {
		ValidResult preValid = validCheckBeforeInsertOrUpdate(account);
		if (preValid.hasError()) {
			return preValid;
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

	@Override
	public ValidResult validCheckBeforeInsertOrUpdate(SysAccount account) {
		if (StringUtils.isNull(account) || StringUtils.isNull(account.getCategory())
				|| StringUtils.isNull(account.getOpenCode()) || StringUtils.isNull(account.getUserId())) {
			return ValidResult.error("账号、账号类型或用户ID为空");
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
		List<SysAccount> accountList = listByCategoryAndUserId(account);
		if (accountList.size() > 1) {
			return ValidResult.error("同一类型的账号数大于1，严重异常！用户ID：" + account.getUserId());
		}
		return ValidResult.success();
	}

	@Override
	public Integer insertOrUpdateSelective(SysAccount account) {
		List<SysAccount> accountList = listByCategoryAndUserId(account);
		if (accountList.size() == 0) {
			return insertSelective(account);
		}
		if (accountList.size() == 1) {
			SysAccount existAccount = accountList.get(0);
			if (existAccount.getOpenCode().equals(account.getOpenCode())) {
				return 0; // 无需更新
			} else {
				existAccount.setOpenCode(account.getOpenCode());
				return updateSelective(existAccount);
			}
		}
		if (accountList.size() > 1) {
			throw new RuntimeException("同一类型的账号数大于1，严重异常！用户ID：" + account.getUserId());
		}
		return 0;
	}

}
