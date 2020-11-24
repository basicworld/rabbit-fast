package com.rabbit.system.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.rabbit.common.util.StringUtils;
import com.rabbit.system.domain.SysUser;

/**
 * 
 * @author wlfei
 *
 */
public class SysUserDTO {
	/**
	 * SysUser.id
	 */
	private Long userId;
	/**
	 * 昵称 SysUser.name
	 */
	private String nickname;

	/**
	 * 当前密码 SysUser.password
	 */
	private String password;
	/**
	 * 新密码
	 */
	private String newPassword;
	/**
	 * 用户名 SysAccount.openCode
	 */
	private String username;
	/**
	 * 账户类型 SysAccount.category
	 */
	private Integer category;

	private Long deptId;

	private Boolean deleted;

	private Long[] roleIds;

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public SysUserDTO() {
	}

	public SysUserDTO(SysUser user) {
		this.userId = user.getId();
		this.nickname = user.getName();
		if (StringUtils.isNotNull(user.getAccount())) {
			this.username = user.getAccount().getOpenCode();
			this.category = user.getAccount().getCategory();
			this.deleted = user.getAccount().getDeleted();
		}
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Long getUserId() {
		return userId;
	}

	@Size(min = 2, max = 20, message = "昵称长度限制为2-20个字符")
	@NotBlank(message = "昵称不能为空")
	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	@Size(min = 5, max = 30, message = "用户名长度限制为5-30个字符")
	@NotBlank(message = "用户名不能为空")
	public String getUsername() {
		return username;
	}

	@NotNull(message = "账户类型不能为空")
	public Integer getCategory() {
		return category;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

}
