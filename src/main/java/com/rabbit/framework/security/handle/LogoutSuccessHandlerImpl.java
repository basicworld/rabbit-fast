package com.rabbit.framework.security.handle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.alibaba.fastjson.JSON;
import com.rabbit.common.constant.ResultConstants;
import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.framework.manager.AsyncManager;
import com.rabbit.framework.manager.factory.AsyncFactory;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.constant.LogConstants;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(LogoutSuccessHandlerImpl.class);
	@Autowired
	private TokenService tokenService;

	/**
	 * 退出处理
	 * 
	 * @return
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		LoginUser loginUser = tokenService.getLoginUser(request);
		// 返回给用户的信息
		String jString = JSON.toJSONString(AjaxResult.error(ResultConstants.CODE_SUCCESS, "退出成功"));

		if (StringUtils.isNotNull(loginUser)) {
			String username = loginUser.getUsername();
			// 删除用户缓存记录
			logger.debug("用户退出，删除缓存记录...");
			tokenService.delLoginUser(loginUser.getToken());
			// 记录用户退出日志
			AsyncManager.me().execute(AsyncFactory.recordLoginLog(LogConstants.TYPE_LOGOUT, loginUser.getUser().getId(),
					username, true, "退出成功", jString));
		}

		ServletUtils.renderString(response, jString);
	}
}
