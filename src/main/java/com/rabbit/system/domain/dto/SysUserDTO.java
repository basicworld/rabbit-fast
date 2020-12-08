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

	/**
	 * 账号类型
	 * 
	 * @return
	 */
	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	/**
	 * 手机
	 * 
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 邮箱
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 身份证
	 * 
	 * @return
	 */
	public String getIdcard() {
		return idcard;
	}

	/**
	 * 微信
	 * 
	 * @return
	 */
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

	/**
	 * 部门名称
	 * 
	 * @return
	 */
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * 角色ID
	 * 
	 * @return
	 */
	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	/**
	 * 部门ID
	 * 
	 * @return
	 */
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

	/**
	 * 删除标记
	 * 
	 * @return
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * 用户ID
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 昵称，非空，2-30个字符
	 * 
	 * @return
	 */
	@Size(min = 2, max = 20, message = "昵称长度限制为2-20个字符")
	@NotBlank(message = "昵称不能为空")
	public String getNickname() {
		return nickname;
	}

	/**
	 * 密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 新密码
	 * 
	 * @return
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * 用户名，非空，5-30个字符
	 * 
	 * @return
	 */
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
