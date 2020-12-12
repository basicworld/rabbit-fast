package com.rabbit.framework.manager.factory;

import java.util.Date;
import java.util.TimerTask;

import com.rabbit.common.util.spring.SpringUtils;
import com.rabbit.system.monitor.domain.SysLog;
import com.rabbit.system.monitor.service.ISysLogService;

/**
 * 异步工厂
 * 
 * @author wlfei
 *
 */
public class AsyncFactory {
	/**
	 * 记录登录、退出日志
	 * 
	 * @param operType  操作类型
	 * @param userId    用户id
	 * @param username  用户名
	 * @param isSuccess 成功标记
	 * @param content   动作描述
	 * @param msg       返回信息
	 * @return
	 */
	public static TimerTask recordLoginLog(final String operType, final Long userId, final String username,
			final boolean isSuccess, final String content, final String msg) {
		return new TimerTask() {
			@Override
			public void run() {
				SysLog sysLog = new SysLog();
				sysLog.setUserId(userId);
				sysLog.setUserName(username);
				sysLog.setIsSuccess(isSuccess);
				sysLog.setOperTime(new Date());
				sysLog.setOperType(operType);
				sysLog.setOperContent(content);
				sysLog.setReturnMsg(msg);
				SpringUtils.getBean(ISysLogService.class).insertSelective(sysLog);
			}
		};
	}

	/**
	 * 记录操作日志
	 * 
	 * @param sysLog
	 * @return
	 */
	public static TimerTask recordOperLog(final SysLog sysLog) {
		return new TimerTask() {

			@Override
			public void run() {
				SpringUtils.getBean(ISysLogService.class).insertSelective(sysLog);
			}
		};
	}
}
