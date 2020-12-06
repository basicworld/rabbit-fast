package com.rabbit.system.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String salt;

	private String password;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private Boolean deleted;
	/**
	 * 当前登录、编辑账号
	 */
	private SysAccount account;
	/**
	 * 用户所有账号
	 */
	private List<SysAccount> allAccounts;
	/**
	 * 角色ID
	 */
	private Long[] roleIds;
	/**
	 * 部门ID
	 */
	private Long deptId;

	public List<SysAccount> getAllAccounts() {
		return allAccounts;
	}

	public void setAllAccounts(List<SysAccount> allAccounts) {
		this.allAccounts = allAccounts;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public SysUser() {
		super();
	}

	public SysAccount getAccount() {
		return account;
	}

	public void setAccount(SysAccount account) {
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt == null ? null : salt.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy == null ? null : createBy.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "SysUser [id=" + id + ", name=" + name + ", salt=" + salt + ", password=" + password + ", deleted="
				+ deleted + ", account=" + account + ", allAccounts=" + allAccounts + ", roleIds="
				+ Arrays.toString(roleIds) + ", deptId=" + deptId + "]";
	}

}