package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysRole;
import com.rabbit.system.domain.SysRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 角色mapper
 * 
 * @author wlfei
 *
 */
public interface SysRoleMapper {
	int deleteByExample(SysRoleExample example);

	int deleteByPrimaryKey(Long id);

	int insert(SysRole record);

	int insertSelective(SysRole record);

	List<SysRole> selectByExample(SysRoleExample example);

	SysRole selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") SysRole record, @Param("example") SysRoleExample example);

	int updateByExample(@Param("record") SysRole record, @Param("example") SysRoleExample example);

	int updateByPrimaryKeySelective(SysRole record);

	int updateByPrimaryKey(SysRole record);
}