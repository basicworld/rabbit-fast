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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.common.constant.ResultConstants;
import com.rabbit.common.core.text.StrFormatter;
import com.rabbit.common.util.BCryptUtils;
import com.rabbit.common.util.RSAUtils;
import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.aspectj.annotation.Log;
import com.rabbit.framework.security.domain.LoginBody;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.SysLoginService;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.constant.LogConstants;
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
	private static final Logger logger = LoggerFactory.getLogger(SysPersonalController.class);
	// 全局rsa公钥
	@Value("${rsa.publicKey}")
	private String rsaPublicKey;
	// 全局rsa私钥
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
		logger.debug("user login...");
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
		return AjaxResult.success(ResultConstants.MESSAGE_SUCCESS, token);
	}

	/**
	 * 更新密码
	 * 
	 * @param userDTO
	 * @return
	 */
	@Log(operateType = LogConstants.TYPE_CHANGE_PWD, isSaveRequestData = false)
	@PutMapping("/password")
	public AjaxResult updatePassword(@RequestBody SysUserDTO userDTO) {
		logger.debug("user change password...");
		if (StringUtils.isNull(userDTO.getNewPassword()) || StringUtils.isNull(userDTO.getPassword())) {
			return AjaxResult.error("原密码、新密码不能为空");
		}
		// 明文 原始密码
		String oldPassword = RSAUtils.decrypt(rsaPrivateKey, userDTO.getPassword());
		// 明文 新密码
		String newPassword = RSAUtils.decrypt(rsaPrivateKey, userDTO.getNewPassword());

		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		// 从数据库获取用户密码，解决前端第二次修改密码报错bug
		SysUser userInDB = userService.selectByPrimaryKey(loginUser.getUser().getId());

		boolean isOldpasswordValid = BCryptUtils.isSamePassword(oldPassword, userInDB.getPassword());
		if (!isOldpasswordValid) {
			return AjaxResult.error("原密码错误");
		}
		if (oldPassword.equals(newPassword)) {
			return AjaxResult.error("新旧密码相同，无需更新");
		}
		// 加密后的新密码
		String encodeNewPassword = BCryptUtils.encode(newPassword);
		// 根据用户id更新密码
		SysUser user = new SysUser(); // 更新提交用
		user.setId(loginUser.getUser().getId());
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
	@Log(operateType = LogConstants.TYPE_CHANGE_INFO)
	@PutMapping("/info")
	public AjaxResult updateInfo(@RequestBody SysUserDTO userDTO) {
		logger.debug("user change userinfo...");
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
		// 校验待更新参数是否合法
		ValidResult result = userService.validCheckBeforeUpdate(user);
		if (result.hasError()) {
			return AjaxResult.error(result.getMessage());
		}
		// 更新用户
		user.setPassword(null);
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
		logger.debug("普通用户获取用户详情...");
		LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
		SysUser user = loginUser.getUser();
		// 获取角色CODE
		Set<String> roles = roleService.listByUserId(user.getId()).stream().map(v -> v.getCode())
				.collect(Collectors.toSet());

		if (StringUtils.isEmpty(roles)) {
			String warning = StrFormatter.format("用户ID为{}的角色为空", user.getId());
			logger.warn(warning);
			// 添加一个空角色标示，防止前台角色为空时的请求风暴
			roles.add("__empty__");
		}
		// 返回值构造
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("roles", roles);
		data.put("avatar", "");
		data.put("introduction", "");
		// 解决用户部分信息更新时，前台不能及时显示的问题
		SysUser userInDB = userService.selectByPrimaryKey(user.getId());
		// 获取用户的所有账号
		List<SysAccount> accounts = accountService.listByUserId(user.getId());
		userInDB.setAllAccounts(accounts);
		SysUserDTO dto = userService.user2dto(userInDB);
		data.put("nickname", dto.getNickname());
		data.put("phone", dto.getPhone());
		data.put("email", dto.getEmail());

		return AjaxResult.success(data);
	}
}
