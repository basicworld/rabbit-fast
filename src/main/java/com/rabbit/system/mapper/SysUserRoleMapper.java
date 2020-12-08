package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysUserRole;
import com.rabbit.system.domain.SysUserRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 用户--角色mapper
 * 
 * @author wlfei
 *
 */
public interface SysUserRoleMapper {
	int deleteByExample(SysUserRoleExample example);

	int deleteByPrimaryKey(Long id);

	int insert(SysUserRole record);

	int insertSelective(SysUserRole record);

	List<SysUserRole> selectByExample(SysUserRoleExample example);

	SysUserRole selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") SysUserRole record, @Param("example") SysUserRoleExample example);

	int updateByExample(@Param("record") SysUserRole record, @Param("example") SysUserRoleExample example);

	int updateByPrimaryKeySelective(SysUserRole record);

	int updateByPrimaryKey(SysUserRole record);
}