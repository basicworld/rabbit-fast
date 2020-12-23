package com.rabbit.framework.security.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.common.constant.BaseConstants;
import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.StringUtils;
import com.rabbit.framework.redis.RedisCache;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.system.constant.ConfigConstants;
import com.rabbit.system.service.ISysConfigService;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token验证处理
 * 
 * @author ruoyi
 */
@Component
public class TokenService {
	@Autowired
	ISysConfigService configService;

	protected static final long MILLIS_SECOND = 1000;

	protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

	private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

	@Autowired
	private RedisCache redisCache;

	/**
	 * 获取用户身份信息
	 * 
	 * @return 用户信息
	 */
	public LoginUser getLoginUser(HttpServletRequest request) {
		// 获取请求携带的令牌
		String token = getToken(request);
		if (StringUtils.isNotEmpty(token)) {
			Claims claims = parseToken(token);
			// 解析对应的权限以及用户信息
			String uuid = (String) claims.get(BaseConstants.LOGIN_USER_KEY);
			String userKey = getTokenKey(uuid);
			// 获取redis存储的token
			LoginUser user = redisCache.getCacheObject(userKey);
			return user;
		}
		return null;
	}

	/**
	 * 设置用户身份信息
	 */
	public void setLoginUser(LoginUser loginUser) {
		if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
			refreshToken(loginUser);
		}
	}

	/**
	 * 删除用户身份信息
	 */
	public void delLoginUser(String token) {
		if (StringUtils.isNotEmpty(token)) {
			String userKey = getTokenKey(token);
			redisCache.deleteObject(userKey);
		}
	}

	/**
	 * 创建令牌
	 * 
	 * @param loginUser 用户信息
	 * @return 令牌
	 */
	public String createToken(LoginUser loginUser) {
		String token = UUID.randomUUID().toString();
		loginUser.setToken(token);
		setUserAgent(loginUser);
		refreshToken(loginUser);

		Map<String, Object> claims = new HashMap<>();
		claims.put(BaseConstants.LOGIN_USER_KEY, token);
		return createToken(claims);
	}

	/**
	 * 验证令牌有效期，相差不足20分钟，自动刷新缓存
	 * 
	 * @param token 令牌
	 * @return 令牌
	 */
	public void verifyToken(LoginUser loginUser) {
		long expireTime = loginUser.getExpireTime();
		long currentTime = System.currentTimeMillis();
		if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
			refreshToken(loginUser);
		}
	}

	/**
	 * 刷新令牌有效期
	 * 
	 * @param loginUser 登录信息
	 */
	public void refreshToken(LoginUser loginUser) {
		int EXPIRE_TIME = Integer
				.parseInt(configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_TOKEN_EXPIRE_TIME));
		loginUser.setLoginTime(System.currentTimeMillis());
		loginUser.setExpireTime(loginUser.getLoginTime() + EXPIRE_TIME * MILLIS_MINUTE);
		// 根据uuid将loginUser缓存
		String userKey = getTokenKey(loginUser.getToken());
		redisCache.setCacheObject(userKey, loginUser, EXPIRE_TIME, TimeUnit.MINUTES);
	}

	/**
	 * 设置用户代理信息
	 * 
	 * @param loginUser 登录信息
	 */
	public void setUserAgent(LoginUser loginUser) {
		UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
		String ip = "";
		loginUser.setIpaddr(ip);
		loginUser.setLoginLocation("");
		loginUser.setBrowser(userAgent.getBrowser().getName());
		loginUser.setOs(userAgent.getOperatingSystem().getName());
	}

	/**
	 * 从数据声明生成令牌
	 *
	 * @param claims 数据声明
	 * @return 令牌
	 */
	private String createToken(Map<String, Object> claims) {
		final String SECRET = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_TOKEN_SECRET);
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return token;
	}

	/**
	 * 从令牌中获取数据声明
	 *
	 * @param token 令牌
	 * @return 数据声明
	 */
	private Claims parseToken(String token) {
		final String SECRET = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_TOKEN_SECRET);
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	/**
	 * 从令牌中获取用户名
	 *
	 * @param token 令牌
	 * @return 用户名
	 */
	public String getUsernameFromToken(String token) {
		Claims claims = parseToken(token);
		return claims.getSubject();
	}

	/**
	 * 获取请求token
	 *
	 * @param request
	 * @return token
	 */
	private String getToken(HttpServletRequest request) {
		final String HEADER = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_TOKEN_HEADER);
		String token = request.getHeader(HEADER);
		if (StringUtils.isNotEmpty(token) && token.startsWith(BaseConstants.TOKEN_PREFIX)) {
			token = token.replace(BaseConstants.TOKEN_PREFIX, "");
		}
		return token;
	}

	private String getTokenKey(String uuid) {
		return BaseConstants.LOGIN_TOKEN_KEY + uuid;
	}
}
