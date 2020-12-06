package com.rabbit.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.common.constant.ResultConstants;
import com.rabbit.common.core.text.StrFormatter;
import com.rabbit.common.util.RSAUtils;
import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.security.domain.LoginBody;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.SysLoginService;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.domain.Captcha;
import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysUser;
import com.rabbit.system.domain.dto.SysUserDTO;
import com.rabbit.system.service.ICaptchaService;
import com.rabbit.system.service.ISysAccountService;
import com.rabbit.system.service.ISysRoleService;
import com.rabbit.system.service.ISysUserRoleService;
import com.rabbit.system.service.ISysUserService;

/**
 * 个人登录、退出、修改密码、更新信息
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping("/personal")
public class SysPersonalController {
	protected final Logger logger = LoggerFactory.getLogger(SysPersonalController.class);

	@Value("${rsa.publicKey}")
	private String rsaPublicKey;

	@Value("${rsa.privateKey}")
	private String rsaPrivateKey;

	@Autowired
	private TokenService tokenService;
	@Autowired
	private SysLoginService loginService;
	@Autowired
	ISysRoleService roleService;
	@Autowired
	ICaptchaService captchaService;
	@Autowired
	ISysAccountService accountService;

	@Autowired
	ISysUserService userService;

	@Autowired
	ISysUserRoleService userRoleService;

	/**
	 * 登录方法
	 * 
	 * @param loginBody 登录信息
	 * @return 结果
	 */
	@PostMapping("/login")
	public AjaxResult login(@RequestBody LoginBody loginBody) {
		// 验证码处置
		Captcha cap = new Captcha();
		cap.setCode(loginBody.getCode());
		cap.setUuid(loginBody.getUuid());
		if (!captchaService.validate(cap)) {
			return AjaxResult.error("验证码错误");
		}

		// 用户名密码验证，并生成token
		String password = RSAUtils.decrypt(rsaPrivateKey, loginBody.getPassword());
		loginBody.setPassword(password);
		logger.debug("用户登录，参数：" + loginBody);
		String token = loginService.login(loginBody.getUsername(), loginBody.getPassword());
		return new AjaxResult(ResultConstants.CODE_SUCCESS, ResultConstants.MESSAGE_SUCCESS, token);
	}

	/**
	 * 更新密码
	 * 
	 * @param userDTO
	 * @return
	 */
	@PutMapping("/password")
	public AjaxResult updatePassword(@RequestBody SysUserDTO userDTO) {
		logger.debug("普通用户更新密码");
		if (StringUtils.isNull(userDTO.getNewPassword()) || StringUtils.isNull(userDTO.getPassword())) {
			return AjaxResult.error("原密码、新密码不能为空");
		}
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUser user = new SysUser(); // 更新提交用
		user.setId(loginUser.getUser().getId());

		String oldPassword = RSAUtils.decrypt(rsaPrivateKey, userDTO.getPassword());
		String newPassword = RSAUtils.decrypt(rsaPrivateKey, userDTO.getNewPassword());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean isOldpasswordValid = encoder.matches(oldPassword, loginUser.getUser().getPassword());
		if (!isOldpasswordValid) {
			return AjaxResult.error("原密码错误");
		}
		String encodeNewPassword = encoder.encode(newPassword);
		user.setPassword(encodeNewPassword);
		userService.updateSelective(user);
		return AjaxResult.success();
	}

	/**
	 * 更新用户信息
	 * 
	 * @param userDTO
	 * @return
	 */
	@PutMapping("/info")
	public AjaxResult updateInfo(@RequestBody SysUserDTO userDTO) {
		logger.debug("普通用户更新信息");
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUserDTO updateDTO = new SysUserDTO(); // 更新提交用
		updateDTO.setUserId(loginUser.getUser().getId());
		// 只允许更新昵称、手机号、邮箱
		updateDTO.setNickname(userDTO.getNickname());
		updateDTO.setPhone(userDTO.getPhone());
		updateDTO.setEmail(userDTO.getEmail());
		updateDTO.setUsername(loginUser.getUsername());// 用户名不能删除
		SysUser user = userService.dto2User(updateDTO);
		logger.info("待更新用户信息：" + user);

		ValidResult result = userService.validCheckBeforeUpdate(user);
		if (result.hasError()) {
			return AjaxResult.error(result.getMessage());
		}
		userService.updateSelective(user);

		return AjaxResult.success();
	}

	/**
	 * 获取用户信息
	 * 
	 * @return 用户信息
	 */
	@GetMapping("/info")
	public AjaxResult getInfo() {
		logger.debug("普通用户获取用户详情");
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUser user = loginUser.getUser();
		Long[] roleIds = userRoleService.listByUserId(user.getId()).stream().map(v -> v.getRoleId())
				.toArray(Long[]::new);
		Set<String> roles = roleService.listByPrimaryKeys(roleIds).stream().map(v -> v.getCode())
				.collect(Collectors.toSet());
		if (StringUtils.isEmpty(roles)) {
			String warning = StrFormatter.format("用户ID为{}的角色为空", user.getId());
			logger.warn(warning);
			// 添加一个空角色标示，防止前台角色为空时的请求风暴
			roles.add("__empty__");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("roles", roles);
		data.put("avatar", "");
		data.put("introduction", "");
		// 解决用户部分信息更新时，前台不能及时显示的问题
		SysUser userInDB = userService.selectByPrimaryKey(user.getId());
		List<SysAccount> accounts = accountService.listByUserId(user.getId());
		userInDB.setAllAccounts(accounts);
		SysUserDTO dto = userService.user2dto(userInDB);
		data.put("nickname", dto.getNickname());
		data.put("phone", dto.getPhone());
		data.put("email", dto.getEmail());

		return AjaxResult.success(data);
	}
}
