package com.rabbit.system.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SysDept {
	private Long id;

	private Long parentId;

	private String fullname;

	private String name;

	private String orgcode;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private Boolean deleted;

	private List<SysDept> children = new ArrayList<SysDept>();

	private Long[] roleIds;

	private Long[] userIds;

	/**
	 * 关联角色ID
	 * 
	 * @return
	 */
	public Long[] getRoleIds() {
		return roleIds;
	}

	/**
	 * 关联用户ID
	 * 
	 * @return
	 */
	public Long[] getUserIds() {
		return userIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}

	/**
	 * 子部门列表
	 * 
	 * @return
	 */
	public List<SysDept> getChildren() {
		return children;
	}

	public void setChildren(List<SysDept> children) {
		this.children = children;
	}

	/**
	 * 部门主键
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 父ID
	 * 
	 * @return
	 */
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 部门全称，最多50字
	 * 
	 * @return
	 */
	@Size(min = 0, max = 50, message = "部门全称长度不能超过50个字符")
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname == null ? null : fullname.trim();
	}

	/**
	 * 部门简称，最多50字
	 * 
	 * @return
	 */
	@Size(min = 0, max = 50, message = "部门简称长度不能超过50个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/**
	 * 机构代码，不能为空
	 * 
	 * @return
	 */
	@NotBlank(message = "机构代码不能为空")
	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode == null ? null : orgcode.trim();
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
}