package com.rabbit.system.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * 
 * @author wlfei
 *
 */
public class SysMenu {
	private Long id;

	private Long parentId;

	private String name;

	private String path;

	private String component;

	private Integer orderNum;

	private Boolean isFrame;

	private Boolean visible;

	private Boolean status;

	private String perms;

	private String icon;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private Boolean deleted;

	private List<SysMenu> children = new ArrayList<SysMenu>();

	/**
	 * 子菜单列表
	 * 
	 * @return
	 */
	public List<SysMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}

	/**
	 * 主键
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
	 * 菜单名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/**
	 * 菜单路由
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path == null ? null : path.trim();
	}

	/**
	 * 前端组件
	 * 
	 * @return
	 */
	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component == null ? null : component.trim();
	}

	/**
	 * 排序
	 * 
	 * @return
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 是否外链
	 * 
	 * @return
	 */
	public Boolean getIsFrame() {
		return isFrame;
	}

	public void setIsFrame(Boolean isFrame) {
		this.isFrame = isFrame;
	}

	/**
	 * 菜单可见性
	 * 
	 * @return
	 */
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	/**
	 * 状态
	 * 
	 * @return
	 */
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * 权限标识
	 * 
	 * @return
	 */
	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms == null ? null : perms.trim();
	}

	/**
	 * 图标
	 * 
	 * @return
	 */
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon == null ? null : icon.trim();
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