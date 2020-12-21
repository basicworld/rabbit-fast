package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rabbit.framework.redis.RedisCache;
import com.rabbit.system.domain.SysConfig;
import com.rabbit.system.domain.SysConfigExample;
import com.rabbit.system.mapper.SysConfigMapper;
import com.rabbit.system.service.ISysConfigService;

@Service
public class SysConfigServiceImpl implements ISysConfigService {
	@Autowired
	SysConfigMapper configMapper;

	@Autowired
	ConfigurableEnvironment environment;

	private static final String REDIS_PREFIX = "sys_config:";

	@Autowired
	private RedisCache redisCache;

	@Override
	public List<SysConfig> listAll() {
		return configMapper.selectByExample(new SysConfigExample());
	}

	@Override
	public Integer loadAllToCache() {
		List<SysConfig> configs = this.listAll();
		for (SysConfig conf : configs) {
			String redisKey = this.getRedisConfigKey(conf.getCkey());
			redisCache.setCacheObject(redisKey, conf.getCvalue());
		}
		return configs.size();
	}

	private String getRedisConfigKey(String rawConfigKey) {
		return REDIS_PREFIX + rawConfigKey;
	}

	@Override
	public Object selectByConfigKeyFromCache(String configKey) {
		Object redisResult = redisCache.getCacheObject(this.getRedisConfigKey(configKey));

		return redisResult;
	}

	@Override
	public Integer insertSelective(SysConfig item) {
		// TODO Auto-generated method stub
		item.setCreateTime(new Date());
		item.setDeleted(false);
		return configMapper.insertSelective(item);
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		return configMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateSelective(SysConfig item) {
		// TODO Auto-generated method stub
		item.setUpdateTime(new Date());
		return configMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysConfig selectByPrimaryKey(Long id) {
		return configMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public Integer updateByConfigKey(List<SysConfig> configList) {
		Integer result = 0;
		for (SysConfig item : configList) {
			SysConfigExample example = new SysConfigExample();
			example.createCriteria().andCkeyEqualTo(item.getCkey().trim());
			
			// 只能更新key、value这两个参数
			SysConfig updatePram = new SysConfig();
			updatePram.setCkey(item.getCkey().trim());
			updatePram.setCvalue(item.getCvalue().trim());
			result += configMapper.updateByExampleSelective(updatePram, example);
		}
		return result;
	}

}
