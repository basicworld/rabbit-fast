package com.rabbit.system.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.framework.web.page.TableDataInfo;
import com.rabbit.system.base.BaseController;
import com.rabbit.system.constant.LogConstants;
import com.rabbit.system.monitor.domain.SysLog;
import com.rabbit.system.monitor.service.ISysLogService;

/**
 * 日志 controller
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping("/system/log")
public class SysLogController extends BaseController {
	@Autowired
	ISysLogService logService;

	@GetMapping("/logtype/list")
	public AjaxResult getLogType() {
		return AjaxResult.success(LogConstants.ALL_OPER_TYPE_LIST);
	}

	@GetMapping("/list")
	public TableDataInfo list(SysLog log) {
		startPage();
		List<SysLog> logs = logService.listBySysLog(log);
		return getDataTable(logs);
	}

	/**
	 * 获取角色详情
	 * 
	 * @param roleId
	 * @return
	 */
	@GetMapping("/{logId}")
	public AjaxResult getDetail(@PathVariable Long logId) {

		SysLog log = logService.selectByPrimaryKey(logId);
		return AjaxResult.success(log);
	}

}
