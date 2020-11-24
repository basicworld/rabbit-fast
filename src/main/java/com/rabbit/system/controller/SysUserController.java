package com.rabbit.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.rabbit.common.util.BCryptUtils;
import com.rabbit.common.util.SecurityUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TableDataInfo;
import com.rabbit.system.base.BaseController;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysUserService;

@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
	protected final Logger logger = LoggerFactory.getLogger(SysUserController.class);
	@Autowired
	ISysUserService userService;
	@Autowired
	ISysAccountService accountService;
	/**
	 * 默认明文密码
	 */
	@Value("${user.defaultPassword}")
	private String DEFAULT_RAW_PASSWORD;

	@GetMapping("/list")
	public TableDataInfo list(SysUserDTO userDTO) {
		startPage();
		List<SysAccount> accountList = accountService.listByUserDTO(userDTO);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		Long total = new PageInfo(accountList).getTotal();
		List<SysUser> userList = userService.account2User(accountList);
		logger.debug("userList: " + userList);
		List<SysUserDTO> userDTOList = new ArrayList<SysUserDTO>();
		for (SysUser user : userList) {
			userDTOList.add(new SysUserDTO(user));
		}
		return getDataTable(userDTOList, total);
	}

	/**
	 * 新增
	 * 
	 * @param userDTO
	 * @return
	 */
	@PostMapping
	public AjaxResult add(@Validated @RequestBody SysUserDTO userDTO) {
		// 删除状态设置
		userDTO.setDeleted(false);
		// 初始化密码设置
		String salt = SecurityUtils.genSalt();
		String encodePassword = BCryptUtils.encode(DEFAULT_RAW_PASSWORD + salt);
		userDTO.setPassword(encodePassword);
		SysUser user = new SysUser(userDTO);
		user.setSalt(salt);

		ValidResult checkResult = userService.validCheckBeforeInsert(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		userService.insertSelective(user);
		return AjaxResult.success();
	}

	/**
	 * 删除
	 * 
	 * @param userId
	 * @return
	 */
	@DeleteMapping("/{userId}")
	public AjaxResult delete(@PathVariable Long userId) {
		ValidResult checkResult = userService.validCheckBeforeDelete(userId);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		userService.deleteByPrimaryKeyLogically(userId);
		return AjaxResult.success();
	}

	/**
	 * 更新
	 * 
	 * @param userDTO
	 * @return
	 */
	@PutMapping
	public AjaxResult update(@Validated @RequestBody SysUserDTO userDTO) {
		SysUser user = new SysUser(userDTO);
		ValidResult checkResult = userService.validCheckBeforeUpdate(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		userService.updateSelective(user);
		return AjaxResult.success();
	}

}
