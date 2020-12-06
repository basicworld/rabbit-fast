package com.rabbit.system.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
	 * 登录账号类型
	 */
	private Integer category;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 身份证
	 */
	private String idcard;
	/**
	 * 微信
	 */
	private String wechat;
	/**
	 * 部门ID
	 */
	private Long deptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 删除标记
	 */
	private Boolean deleted;

	private Long[] roleIds;

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getIdcard() {
		return idcard;
	}

	public String getWechat() {
		return wechat;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

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

}
