package com.rabbit.system.monitor.service;

import java.util.List;

import com.rabbit.system.base.BaseService;
import com.rabbit.system.monitor.domain.SysLog;

/**
 * 系统日志service
 * 
 * @author wlfei
 *
 */
public interface ISysLogService extends BaseService<SysLog> {
	/**
	 * 批量删除日志
	 * 
	 * @param logIds
	 * @return
	 */
	Integer deleteByPrimaryKey(Long[] logIds);

	/**
	 * 自定义条件查询<br>
	 * 查询条件：用户ID、用户名称、操作类型、操作结果
	 * 
	 * @param sysLog
	 * @return
	 */
	List<SysLog> listBySysLog(SysLog sysLog);
}
