package com.rabbit.system.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.domain.SysConfig;

/**
 * 系统配置service
 * 
 * @author wlfei
 *
 */
public interface ISysConfigService extends BaseService<SysConfig> {
	/**
	 * 将数据库所有配置加载到缓存
	 * <p>
	 * 二次运行会覆盖上一次的结果
	 * 
	 * @return
	 */
	Integer loadAllToCache();

	/**
	 * 按ckey批量修改cvalue
	 * 
	 * @param configList
	 * @return
	 */
	Integer updateByConfigKey(List<SysConfig> configList);

	/**
	 * 从数据库获取所有配置属性
	 * 
	 * @return
	 */
	List<SysConfig> listAll();

	/**
	 * 根据键名查询，从缓存查询数据
	 * <p>
	 * 缓存是spring启动时加载的，只有触发loadAllToCache()才会更新缓存
	 * 
	 * @param configKey
	 * @return
	 */
	Object selectByConfigKeyFromCache(String configKey);

}
