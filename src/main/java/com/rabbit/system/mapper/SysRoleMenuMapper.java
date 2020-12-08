package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysRoleMenu;
import com.rabbit.system.domain.SysRoleMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 角色--菜单mapper
 * 
 * @author wlfei
 *
 */
public interface SysRoleMenuMapper {
	int deleteByExample(SysRoleMenuExample example);

	int deleteByPrimaryKey(Long id);

	int insert(SysRoleMenu record);

	int insertSelective(SysRoleMenu record);

	List<SysRoleMenu> selectByExample(SysRoleMenuExample example);

	SysRoleMenu selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") SysRoleMenu record, @Param("example") SysRoleMenuExample example);

	int updateByExample(@Param("record") SysRoleMenu record, @Param("example") SysRoleMenuExample example);

	int updateByPrimaryKeySelective(SysRoleMenu record);

	int updateByPrimaryKey(SysRoleMenu record);
}