package com.rabbit.system.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户账号<br>
 * 一个用户可以有多个账号，每个类型账号只能有一个
 * 
 * @author wlfei
 *
 */
public class SysAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long userId;

	private String openCode;

	private Integer category;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private Boolean deleted;

	public SysAccount() {
		super();
	}

	/**
	 * 账号主键
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
	 * 用户主键
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 账号名
	 * 
	 * @return
	 */
	public String getOpenCode() {
		return openCode;
	}

	public void setOpenCode(String openCode) {
		this.openCode = openCode == null ? null : openCode.trim();
	}

	/**
	 * 账号类型，如用户名、邮箱、手机号
	 * 
	 * @return
	 */
	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
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