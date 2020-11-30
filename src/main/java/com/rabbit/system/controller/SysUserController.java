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
import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TableDataInfo;
import com.rabbit.system.base.BaseController;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysDeptService;
import com.rabbit.system.service.ISysDeptUserService;
import com.rabbit.system.service.ISysUserRoleService;
import com.rabbit.system.service.ISysUserService;

@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
	protected final Logger logger = LoggerFactory.getLogger(SysUserController.class);
	@Autowired
	ISysUserService userService;
	@Autowired
	ISysAccountService accountService;
	@Autowired
	ISysDeptService deptService;
	@Autowired
	ISysDeptUserService deptUserService;
	@Autowired
	ISysUserRoleService userRoleService;
	/**
	 * 默认明文密码
	 */
	@Value("${user.defaultPassword}")
	private String DEFAULT_RAW_PASSWORD;

	/**
	 * 按账号模糊查询，前端以用户为维度展示
	 * 
	 * @param userDTO
	 * @return
	 */
	@GetMapping("/list")
	public TableDataInfo list(SysUserDTO userDTO) {
		startPage();
		List<SysUser> userList = userService.listByUserDTO(userDTO);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Long total = new PageInfo(userList).getTotal();
		List<SysUserDTO> userDTOList = new ArrayList<SysUserDTO>();
		if (StringUtils.isNotEmpty(userList)) {
			for (SysUser user : userList) {
				user.setAllAccounts(accountService.listByUserId(user.getId()));
				SysUserDTO dto = userService.user2dto(user);
				userDTOList.add(dto);
			}
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
		SysUser user = userService.dto2User(userDTO);
		user.setSalt(salt);
		user.setPassword(encodePassword);

		ValidResult checkResult = userService.validCheckBeforeInsert(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		userService.insertSelective(user);
		return AjaxResult.success("创建成功，默认密码：" + DEFAULT_RAW_PASSWORD);
	}

	/**
	 * 获取详情
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/{userId}")
	public AjaxResult getDetail(@PathVariable Long userId) {
		SysUser user = userService.selectByPrimaryKey(userId);
		if (StringUtils.isNull(user)) {
			return AjaxResult.error("用户不存在");
		}

		List<SysAccount> accounts = accountService.listByUserId(userId);
		user.setAllAccounts(accounts);
		// 添加角色信息
		Long[] roleIds = userRoleService.listByUserId(userId).stream().map(v -> v.getRoleId()).toArray(Long[]::new);
		user.setRoleIds(roleIds);

		SysUserDTO dto = userService.user2dto(user);

		return AjaxResult.success(dto);
	}

	/**
	 * 删除
	 * 
	 * @param userId
	 * @return
	 */
	@DeleteMapping("/{userIds}")
	public AjaxResult delete(@PathVariable Long[] userIds) {
		for (Long userId : userIds) {
			ValidResult checkResult = userService.validCheckBeforeDelete(userId);
			if (checkResult.hasError()) {
				return AjaxResult.error(checkResult.getMessage());
			}
		}
		userService.deleteByPrimaryKeyLogically(userIds);
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
		logger.debug("收到更新用户请求");
		SysUser user = userService.dto2User(userDTO);
		ValidResult checkResult = userService.validCheckBeforeUpdate(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		logger.debug("更新用户信息：" + user);
		userService.updateSelective(user);
		return AjaxResult.success();
	}

	/**
	 * 重置密码
	 * 
	 * @param userDTO
	 * @return
	 */
	@PutMapping("/resetPassword")
	public AjaxResult resetPassword(@RequestBody SysUserDTO userDTO) {
		if (StringUtils.isNull(userDTO.getUserId()) || StringUtils.isNull(userDTO.getPassword())) {
			return AjaxResult.error("用户ID或密码为空");
		}
		SysUser user = new SysUser();
		user.setId(userDTO.getUserId());
		// 密码设置
		String salt = SecurityUtils.genSalt();
		String encodePassword = BCryptUtils.encode(userDTO.getPassword() + salt);
		user.setSalt(salt);
		user.setPassword(encodePassword);

		ValidResult checkResult = userService.validCheckBeforeUpdate(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		userService.updateSelective(user);
		return AjaxResult.success();
	}

}
