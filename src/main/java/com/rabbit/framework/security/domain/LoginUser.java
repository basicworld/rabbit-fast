package com.rabbit.framework.security.domain;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rabbit.system.domain.SysUser;

/**
 * 登录用户身份权限
 * 
 * @author wlfei
 *
 */
public class LoginUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户唯一标识
	 */
	private String token;

	/**
	 * 登陆时间
	 */
	private Long loginTime;

	/**
	 * 过期时间
	 */
	private Long expireTime;

	/**
	 * 登录IP地址
	 */
	private String ipaddr;

	/**
	 * 登录地点
	 */
	private String loginLocation;

	/**
	 * 浏览器类型
	 */
	private String browser;

	/**
	 * 操作系统
	 */
	private String os;
	/**
	 * 权限列表 menu.perms列表
	 */
	private Set<String> permissions;
	/**
	 * 角色code列表
	 */
	private Set<String> roles;
	/**
	 * 用户信息
	 */
	private SysUser user;

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return null;
	}

	@JsonIgnore
	@Override
	public String getPassword() {

		return user.getPassword();
	}

	@Override
	public String getUsername() {

		return user.getAccount().getOpenCode();
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return !user.getDeleted();
	}

	public String getToken() {
		return token;
	}

	public Long getLoginTime() {
		return loginTime;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public String getLoginLocation() {
		return loginLocation;
	}

	public String getBrowser() {
		return browser;
	}

	public String getOs() {
		return os;
	}

	public SysUser getUser() {
		return user;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setLoginTime(Long loginTime) {
		this.loginTime = loginTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public void setLoginLocation(String loginLocation) {
		this.loginLocation = loginLocation;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

}
