package com.rabbit.system.mapper;

import com.rabbit.system.domain.SysConfig;
import com.rabbit.system.domain.SysConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysConfigMapper {
    int deleteByExample(SysConfigExample example);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    List<SysConfig> selectByExample(SysConfigExample example);

    int updateByExampleSelective(@Param("record") SysConfig record, @Param("example") SysConfigExample example);

    int updateByExample(@Param("record") SysConfig record, @Param("example") SysConfigExample example);
}