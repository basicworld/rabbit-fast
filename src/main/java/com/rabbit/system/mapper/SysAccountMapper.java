package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysAccount;
import com.rabbit.system.domain.SysAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 账号mapper
 * 
 * @author wlfei
 *
 */
public interface SysAccountMapper {

	int deleteByExample(SysAccountExample example);

	int deleteByPrimaryKey(Long id);

	int insert(SysAccount record);

	int insertSelective(SysAccount record);

	List<SysAccount> selectByExample(SysAccountExample example);

	SysAccount selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") SysAccount record, @Param("example") SysAccountExample example);

	int updateByExample(@Param("record") SysAccount record, @Param("example") SysAccountExample example);

	int updateByPrimaryKeySelective(SysAccount record);

	int updateByPrimaryKey(SysAccount record);
}