package com.rabbit.system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.valid.ValidUtils;
import com.rabbit.framework.aspectj.annotation.Log;
import com.rabbit.framework.manager.AsyncManager;
import com.rabbit.framework.manager.factory.AsyncFactory;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.constant.LogConstants;
import com.rabbit.system.constant.MailConstants;
import com.rabbit.system.domain.SysConfig;
import com.rabbit.system.service.IMailService;
import com.rabbit.system.service.ISysConfigService;

/**
 * 系统参数controller
 * 
 * @author wlfei
 *
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController {
	private static final Logger logger = LoggerFactory.getLogger(SysConfigController.class);

	@Autowired
	ISysConfigService configService;

	@Autowired
	IMailService mailService;

	/**
	 * 给指定邮箱发送验证邮件
	 * 
	 * @param email
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:config')")
	@GetMapping("/emailCheck/{email}")
	public AjaxResult emailConfigCheck(@PathVariable String email) {
		logger.debug("发送验证邮件给：" + email);
		if (!ValidUtils.isValidEmail(email)) {
			return AjaxResult.error("邮箱格式错误");
		}
		if (!mailService.canSendMail()) {
			return AjaxResult.error("已关闭邮件功能，请打开后重试！");
		}
		AsyncManager.me().execute(AsyncFactory.sendSimpleMail(email.trim(), MailConstants.SUBJECT_OF_TEST,
				MailConstants.CONTENT_OF_TEST));
		return AjaxResult.success(StringUtils.format("尝试发送邮件给{}，请注意查收", email));
	}

	/**
	 * 获取参数列表
	 * 
	 * @return
	 */
	@PreAuthorize("@ss.hasPermi('system:config')")
	@GetMapping("/list")
	public AjaxResult listAll() {
		List<SysConfig> list = configService.listAll();
		return AjaxResult.success(list);
	}

	/**
	 * 刷新缓存参数，让更新的参数立即生效
	 * 
	 * @return
	 */
	@Log(operateType = LogConstants.TYPE_RECACHE_CONFIG)
	@PreAuthorize("@ss.hasPermi('system:config')")
	@GetMapping("/recache")
	public AjaxResult reCache() {
		logger.info("刷新数据库缓存");
		// 缓存重新加载
		configService.loadAllToCache();
		// 更新依赖缓存的相关服务
		// - 更新邮件配置
		mailService.reloadMailConfigFromCache();
		return AjaxResult.success();
	}

	/**
	 * 更新参数列表
	 * 
	 * @param configList
	 * @return
	 */
	@Log(operateType = LogConstants.TYPE_EDIT_CONFIG)
	@PreAuthorize("@ss.hasPermi('system:config')")
	@PutMapping
	public AjaxResult updateMultipleConfig(@RequestBody List<SysConfig> configList) {
		logger.info("更新参数配置," + configList);
		configService.updateByConfigKey(configList);
		return AjaxResult.success();
	}

}
