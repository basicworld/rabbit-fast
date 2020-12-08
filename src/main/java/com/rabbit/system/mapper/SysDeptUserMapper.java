package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysDeptUser;
import com.rabbit.system.domain.SysDeptUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 部门--用户mapper
 * 
 * @author wlfei
 *
 */
public interface SysDeptUserMapper {
	int deleteByExample(SysDeptUserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(SysDeptUser record);

	int insertSelective(SysDeptUser record);

	List<SysDeptUser> selectByExample(SysDeptUserExample example);

	SysDeptUser selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") SysDeptUser record, @Param("example") SysDeptUserExample example);

	int updateByExample(@Param("record") SysDeptUser record, @Param("example") SysDeptUserExample example);

	int updateByPrimaryKeySelective(SysDeptUser record);

	int updateByPrimaryKey(SysDeptUser record);
}