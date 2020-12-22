package com.rabbit.system.monitor.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.sql.SqlUtil;
import com.rabbit.system.monitor.domain.SysLog;
import com.rabbit.system.monitor.domain.SysLogExample;
import com.rabbit.system.monitor.mapper.SysLogMapper;
import com.rabbit.system.monitor.service.ISysLogService;

@Service
public class SysLogServiceImpl implements ISysLogService {
	@Autowired
	SysLogMapper logMapper;

	static final int MAX_STRING_LEN = 1800;

	@Override
	public Integer insertSelective(SysLog item) {
		if (StringUtils.isNotNull(item.getOperContent()) && item.getOperContent().length() > MAX_STRING_LEN) {
			item.setOperContent(item.getOperContent().substring(0, MAX_STRING_LEN) + "...");
		}
		if (StringUtils.isNotNull(item.getOperArgs()) && item.getOperArgs().length() > MAX_STRING_LEN) {
			item.setOperArgs(item.getOperArgs().substring(0, MAX_STRING_LEN));
		}
		if (StringUtils.isNotNull(item.getReturnMsg()) && item.getReturnMsg().length() > MAX_STRING_LEN) {
			item.setReturnMsg(item.getReturnMsg().substring(0, MAX_STRING_LEN) + "...");
		}
		return logMapper.insertSelective(item);
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		return logMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateSelective(SysLog item) {
		// TODO Auto-generated method stub
		return logMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysLog selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return logMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer deleteByPrimaryKey(Long[] logIds) {
		if (StringUtils.isEmpty(logIds)) {
			return 0;
		}
		List<Long> ids = Stream.of(logIds).collect(Collectors.toList());
		SysLogExample example = new SysLogExample();
		example.createCriteria().andIdIn(ids);
		return logMapper.deleteByExample(example);
	}

	@Override
	public List<SysLog> listBySysLog(SysLog log) {
		// 查询条件：用户ID、用户名称、操作类型、操作结果
		SysLogExample example = new SysLogExample();
		SysLogExample.Criteria c1 = example.createCriteria();
		if (StringUtils.isNotNull(log.getUserId())) {
			c1.andUserIdEqualTo(log.getUserId());
		}
		if (StringUtils.isNotNull(log.getIsSuccess())) {
			c1.andIsSuccessEqualTo(log.getIsSuccess());
		}
		if (StringUtils.isNotEmpty(log.getOperType())) {
			c1.andOperTypeEqualTo(log.getOperType());
		}
		if (StringUtils.isNotEmpty(log.getUserName())) {
			c1.andUserNameLike(SqlUtil.getFuzzQueryParam(log.getUserName()));
		}
		return logMapper.selectByExample(example);
	}

}
