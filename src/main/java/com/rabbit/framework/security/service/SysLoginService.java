package com.rabbit.framework.security.service;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.rabbit.common.constant.ResultConstants;
import com.rabbit.framework.manager.AsyncManager;
import com.rabbit.framework.manager.factory.AsyncFactory;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.constant.LogConstants;

@Component
public class SysLoginService {
	public static Log logger = LogFactory.getLog(SysLoginService.class);

	@Resource
	private AuthenticationManager authenticationManager;

	@Autowired
	TokenService tokenService;

	/**
	 * 登录验证，并返回token
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public String login(final String username, String password) {
		logger.debug("SysLoginService.login: username=" + username + ", password=" + password);
		// 用户验证
		Authentication authentication = null;
		try {
			// 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception e) {
			int code = ResultConstants.CODE_AUTH_FAIL;
			String msg = "认证失败，无法访问资源";
			String errorMsg = JSON.toJSONString(AjaxResult.error(code, msg));
			if (e instanceof BadCredentialsException) {
				// 日志
				AsyncManager.me().execute(AsyncFactory.recordLoginLog(LogConstants.TYPE_LOGIN, null, username, false,
						"用户名与密码不匹配", errorMsg));
				throw new BadCredentialsException("用户名与密码不匹配");
			} else {
				AsyncManager.me().execute(AsyncFactory.recordLoginLog(LogConstants.TYPE_LOGIN, null, username, false,
						e.getMessage(), errorMsg));
				logger.error(e);
				throw e;
			}
		}
		LoginUser loginUser = (LoginUser) authentication.getPrincipal();
		logger.debug("loginUser.user:" + loginUser.getUser());
		String token = tokenService.createToken(loginUser);
		AsyncManager.me().execute(AsyncFactory.recordLoginLog(LogConstants.TYPE_LOGIN, loginUser.getUser().getId(),
				username, true, "登录成功", JSON.toJSONString(AjaxResult.success(ResultConstants.MESSAGE_SUCCESS, token))));
		logger.debug("login success...");
		return token;
	}

}
