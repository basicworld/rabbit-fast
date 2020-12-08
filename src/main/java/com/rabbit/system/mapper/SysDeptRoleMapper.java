package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysDeptRole;
import com.rabbit.system.domain.SysDeptRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 部门--角色mapper
 * 
 * @author wlfei
 *
 */
public interface SysDeptRoleMapper {
	int deleteByExample(SysDeptRoleExample example);

	int deleteByPrimaryKey(Long id);

	int insert(SysDeptRole record);

	int insertSelective(SysDeptRole record);

	List<SysDeptRole> selectByExample(SysDeptRoleExample example);

	SysDeptRole selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") SysDeptRole record, @Param("example") SysDeptRoleExample example);

	int updateByExample(@Param("record") SysDeptRole record, @Param("example") SysDeptRoleExample example);

	int updateByPrimaryKeySelective(SysDeptRole record);

	int updateByPrimaryKey(SysDeptRole record);
}