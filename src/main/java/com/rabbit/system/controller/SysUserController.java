package com.rabbit.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.rabbit.common.util.BaseUtils;
import com.rabbit.common.util.RSAUtils;
import com.rabbit.common.util.SecurityUtils;
import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.aspectj.annotation.Log;
import com.rabbit.framework.manager.AsyncManager;
import com.rabbit.framework.manager.factory.AsyncFactory;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TableDataInfo;
import com.rabbit.system.base.BaseController;
import com.rabbit.system.constant.AccountConstants;
import com.rabbit.system.constant.ConfigConstants;
import com.rabbit.system.constant.LogConstants;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysConfigService;
import com.rabbit.system.service.ISysDeptService;
import com.rabbit.system.service.ISysDeptUserService;
import com.rabbit.system.service.ISysRoleService;
import com.rabbit.system.service.ISysUserRoleService;
import com.rabbit.system.service.ISysUserService;

/**
 * 用户管理controller
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);
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
	@Autowired
	TokenService tokenService;
	@Autowired
	ISysRoleService roleService;
	@Autowired
	ISysConfigService configService;
	/**
	 * 默认明文密码
	 */
	@Value("${user.defaultPassword}")
	private String DEFAULT_RAW_PASSWORD;

	@Value("${rsa.privateKey}")
	private String rsaPrivateKey;

	/**
	 * 获取用户列表，前端以用户为维度展示
	 * 
	 * @param userDTO
	 * @return
	 */
	@GetMapping("/list")
	public TableDataInfo list(SysUserDTO userDTO) {
		logger.debug("获取用户列表...");
		startPage();
		List<SysUser> userList = userService.listByUserDTO(userDTO);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Long total = new PageInfo(userList).getTotal();
		List<SysUserDTO> userDTOList = new ArrayList<SysUserDTO>();
		if (StringUtils.isNotEmpty(userList)) {
			for (SysUser user : userList) {
				// 配置用户账号
				user.setAllAccounts(accountService.listByUserId(user.getId()));
				SysUserDTO dto = userService.user2dto(user);
				userDTOList.add(dto);
			}
		}

		return getDataTable(userDTOList, total);
	}

	/**
	 * 新增用户<br>
	 * 限制：登录用户非管理员的，不能新建包含管理员角色、部门的用户
	 * 
	 * @param userDTO
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:user')")
	@Log(operateType = LogConstants.TYPE_ADD_USER)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody SysUserDTO userDTO) {
		logger.debug("新增用户...");

		// 非管理员不能创建管理员账号
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		boolean loginUserNotAdmin = userService.isNotAdmin(loginUser.getUser().getId());
		if (loginUserNotAdmin) {
			// 角色不能包含管理员
			boolean hasAdminRole = roleService.isContainsAdminRole(userDTO.getRoleIds());
			if (hasAdminRole) {
				return AjaxResult.error("非管理员不能创建管理员账号");
			}
			// 部门不能包含管理员
			boolean iAmAdminDept = deptService.isAdminDept(userDTO.getDeptId());
			if (iAmAdminDept) {
				return AjaxResult.error("非管理员不能调整管理员部门");
			}
		}
		// 删除状态设置
		userDTO.setDeleted(false);
		// 进行加密
		String encodePassword = BCryptUtils.encode(DEFAULT_RAW_PASSWORD);
		// 初始化密码设置
		String salt = SecurityUtils.genSalt(); // salt字段已弃用
		SysUser user = userService.dto2User(userDTO);
		user.setSalt(salt);
		user.setPassword(encodePassword);

		ValidResult checkResult = userService.validCheckBeforeInsert(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		userService.insertSelective(user);
		// 创建用户成功，尝试发送邮件通知
		if (StringUtils.isNotEmpty(userDTO.getEmail())) {
			String subject = (String) configService
					.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SUBJECT_OF_ADD_USER_SUCCESS);
			String content = (String) configService
					.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_CONTENT_OF_ADD_USER_SUCCESS);
			AsyncManager.me().execute(AsyncFactory.sendSimpleMail(userDTO.getEmail(), subject, content));
		}
		return AjaxResult.success("创建成功，默认密码：" + DEFAULT_RAW_PASSWORD);
	}

	/**
	 * 获取用户详情
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/{userId}")
	public AjaxResult getDetail(@PathVariable Long userId) {
		logger.debug("获取用户详情...");

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
	 * 删除用户<br>
	 * 限制：登录用户非管理员的，不能删除管理员用户
	 * 
	 * @param userId
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:user')")
	@Log(operateType = LogConstants.TYPE_DEL_USER)
	@DeleteMapping("/{userIds}")
	public AjaxResult delete(@PathVariable Long[] userIds) {
		logger.debug("删除用户...");

		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		boolean loginUserNotAdmin = userService.isNotAdmin(loginUser.getUser().getId());
		// 删除前校验，一个不通过的，不允许删除
		for (Long userId : userIds) {
			ValidResult checkResult = userService.validCheckBeforeDelete(userId);
			if (checkResult.hasError()) {
				return AjaxResult.error(checkResult.getMessage());
			}

			boolean thisUserAdmin = userService.isAdmin(userId);
			// 非超级管理员不能修改超级管理员信息
			if (loginUserNotAdmin && thisUserAdmin) {
				return AjaxResult.error("非管理员不能删除管理员");
			}
			// 不允许删除当前登录用户
			if (loginUser.getUser().getId().equals(userId)) {
				return AjaxResult.error("不允许删除登录用户账号");
			}
		}
		userService.deleteByPrimaryKeyLogically(userIds);
		return AjaxResult.success();
	}

	/**
	 * 更新用户<br>
	 * 限制：登录用户非管理员的，不能修改管理员用户信息，不能创建管理员用户
	 * 
	 * @param userDTO
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:user')")
	@Log(operateType = LogConstants.TYPE_EDIT_USER)
	@PutMapping
	public AjaxResult update(@Validated @RequestBody SysUserDTO userDTO) {
		logger.debug("更新用户，ID=" + userDTO.getUserId());
		// 不更新密码
		userDTO.setNewPassword(null);
		userDTO.setPassword(null);

		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		// 不能修改当前登录用户
		if (loginUser.getUser().getId().equals(userDTO.getUserId())) {
			return AjaxResult.error("不能修改当前登录用户信息");
		}
		// 非管理员不能调整管理员账号
		boolean loginUserNotAdmin = userService.isNotAdmin(loginUser.getUser().getId());
		if (loginUserNotAdmin) {
			// 不能修改已有的管理员用户
			boolean userIsAdmin = userService.isAdmin(userDTO.getUserId());
			if (userIsAdmin) {
				return AjaxResult.error("非管理员不能修改管理员信息");
			}
			// 更新的角色ID中不能包含管理员角色
			boolean hasAdminRole = roleService.isContainsAdminRole(userDTO.getRoleIds());
			if (hasAdminRole) {
				return AjaxResult.error("非管理员不能创建管理员账号");
			}
			// 更新的部门不能包含管理员角色
			boolean iAmAdminDept = deptService.isAdminDept(userDTO.getDeptId());
			if (iAmAdminDept) {
				return AjaxResult.error("非管理员不能调整管理员部门");
			}
		}

		if (loginUser.getUser().getId().equals(userDTO.getUserId())) {
			return AjaxResult.error("不允许修改登录用户信息");
		}
		// 更新前校验
		SysUser user = userService.dto2User(userDTO);
		ValidResult checkResult = userService.validCheckBeforeUpdate(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		user.setPassword(null);
		userService.updateSelective(user);

		// 更新用户成功，尝试发送邮件通知
		SysAccount queryParam = new SysAccount();
		queryParam.setCategory(AccountConstants.CATEGORY_EMAIL);
		queryParam.setUserId(userDTO.getUserId());
		SysAccount emailAccount = BaseUtils.firstItemOfList(accountService.listByCategoryAndUserId(queryParam));
		if (StringUtils.isNotNull(emailAccount)) {
			String subject = (String) configService
					.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SUBJECT_OF_EDIT_USER_SUCCESS);
			String content = (String) configService
					.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_CONTENT_OF_EDIT_USER_SUCCESS);
			AsyncManager.me().execute(AsyncFactory.sendSimpleMail(emailAccount.getOpenCode(), subject, content));
		}

		return AjaxResult.success();
	}

	/**
	 * 重置密码
	 * 
	 * @param userDTO
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:user')")
	@Log(operateType = LogConstants.TYPE_RESET_PWD, isSaveRequestData = false)
	@PutMapping("/resetPassword")
	public AjaxResult resetPassword(@RequestBody SysUserDTO userDTO) {
		logger.debug("重置用户密码...");

		if (StringUtils.isNull(userDTO.getUserId()) || StringUtils.isNull(userDTO.getPassword())) {
			return AjaxResult.error("用户ID或密码为空");
		}
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		// 非超级管理员不能修改超级管理员信息
		if (userService.isNotAdmin(loginUser.getUser().getId())) {
			if (userService.isAdmin(userDTO.getUserId())) {
				return AjaxResult.error("非管理员不能修改管理员信息");
			}
		}

		SysUser user = new SysUser();
		user.setId(userDTO.getUserId());
		// 密码设置
		String salt = SecurityUtils.genSalt(); // 弃用字段
		String rawPassword = RSAUtils.decrypt(rsaPrivateKey, userDTO.getPassword());
		String encodePassword = BCryptUtils.encode(rawPassword);
		user.setSalt(salt);
		user.setPassword(encodePassword);

		ValidResult checkResult = userService.validCheckBeforeUpdate(user);
		if (checkResult.hasError()) {
			return AjaxResult.error(checkResult.getMessage());
		}
		userService.updateSelective(user);

		// 更新用户成功，尝试发送邮件通知
		SysAccount queryParam = new SysAccount();
		queryParam.setCategory(AccountConstants.CATEGORY_EMAIL);
		queryParam.setUserId(userDTO.getUserId());
		SysAccount emailAccount = BaseUtils.firstItemOfList(accountService.listByCategoryAndUserId(queryParam));
		if (StringUtils.isNotNull(emailAccount)) {
			String subject = (String) configService
					.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_SUBJECT_OF_RESET_PASSWORD_SUCCESS);
			String content = (String) configService
					.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_MAIL_CONTENT_OF_RESET_PASSWORD_SUCCESS);
			AsyncManager.me().execute(AsyncFactory.sendSimpleMail(emailAccount.getOpenCode(), subject, content));
		}

		return AjaxResult.success();
	}

}
