package com.rabbit.framework.security.service;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.rabbit.framework.security.domain.LoginUser;

@Component
public class SysLoginService {
	public static Log logger = LogFactory.getLog(SysLoginService.class);

	@Resource
	private AuthenticationManager authenticationManager;

	@Autowired
	TokenService tokenService;

	public String login(final String username, String password) {
		logger.debug("SysLoginService.login: username=" + username + ", password=" + password);
		// 用户验证
		Authentication authentication = null;
		try {
			// 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		LoginUser loginUser = (LoginUser) authentication.getPrincipal();
		logger.debug("login success");
		return tokenService.createToken(loginUser);
	}

}
