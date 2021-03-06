package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysMenu;
import com.rabbit.system.domain.SysMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单mapper
 * 
 * @author wlfei
 *
 */
public interface SysMenuMapper {
	int deleteByExample(SysMenuExample example);

	int deleteByPrimaryKey(Long id);

	int insert(SysMenu record);

	int insertSelective(SysMenu record);

	List<SysMenu> selectByExample(SysMenuExample example);

	SysMenu selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") SysMenu record, @Param("example") SysMenuExample example);

	int updateByExample(@Param("record") SysMenu record, @Param("example") SysMenuExample example);

	int updateByPrimaryKeySelective(SysMenu record);

	int updateByPrimaryKey(SysMenu record);
}