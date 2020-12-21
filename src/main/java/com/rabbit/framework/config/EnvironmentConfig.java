package com.rabbit.framework.config;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import com.rabbit.system.constant.ConfigConstants;
import com.rabbit.system.domain.SysConfig;
import com.rabbit.system.service.ISysConfigService;

/**
 * 从mysql加载配置属性，注入spring
 * 
 * @author wlfei
 *
 */
@Configuration
@Component()
public class EnvironmentConfig {
	private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfig.class);

	@Autowired
	ConfigurableEnvironment environment;

	@Autowired
	ISysConfigService configService;

	/**
	 * 数据库配置参数加载到缓存
	 */
	@PostConstruct
	public void loadAllDBConfigToCache() {
		configService.loadAllToCache();
	}

	/**
	 * 数据库配置参数加载到spring环境
	 * 
	 */
	@PostConstruct
	public void initSystemConfigFromDB() {
		logger.info("开始从数据库初始化配置");
		// 获取系统属性集合
		MutablePropertySources propertySourcesFromSystem = environment.getPropertySources();

		// 从数据库获取自定义变量列表
		Map<String, String> configMapFromDB = configService.listAll().stream()
				.collect(Collectors.toMap(SysConfig::getCkey, SysConfig::getCvalue));

		// 将转换后的列表加入属性中
		Properties propertiesFromDB = new Properties();
		propertiesFromDB.putAll(configMapFromDB);

		logger.debug("propertiesFromDB=" + propertiesFromDB.toString());

		// 将属性转换为属性集合，并指定名称
		PropertiesPropertySource propertySourceFromDB = new PropertiesPropertySource(
				ConfigConstants.NAME_OF_DB_PROPERTY_SOURCE, propertiesFromDB);

		// 定义寻找属性的正则，该正则为系统默认属性集合的前缀
		Pattern p = Pattern.compile("^applicationConfig.*");

		// 接收系统默认属性集合的名称
		String name = null;

		// 标识是否找到系统默认属性集合
		boolean flag = false;

		// 遍历属性集合
		for (PropertySource<?> source : propertySourcesFromSystem) {
			// 正则匹配
			if (p.matcher(source.getName()).matches()) {
				// 接收名称
				name = source.getName();
				// 变更标识
				flag = true;

				break;
			}
		}
		if (flag) {
			// 找到则将自定义属性添加到该属性之前
			// 找到则将自定义属性添加到该属性之后，意思就是以application.properties文件配置为准 如果想要以数据库配置为准，就修改为
			// propertySources.addBefore(name, constants)
			propertySourcesFromSystem.addBefore(name, propertySourceFromDB);
			logger.info("数据库配置参数添加到spring");
		} else {
			// 没找到默认添加到第一位
			propertySourcesFromSystem.addFirst(propertySourceFromDB);
		}

		logger.info("完成从数据库初始化配置");
		logger.info(propertySourcesFromSystem.toString());
		String emailUsername = (String) propertySourcesFromSystem.get(ConfigConstants.NAME_OF_DB_PROPERTY_SOURCE)
				.getProperty(ConfigConstants.KEY_OF_MAIL_SMTP_USERNAME);
		logger.debug("emailUsername=" + emailUsername);
		String emailHost = (String) propertySourcesFromSystem.get(ConfigConstants.NAME_OF_DB_PROPERTY_SOURCE)
				.getProperty(ConfigConstants.KEY_OF_MAIL_SMTP_HOST);
		logger.debug("emailUsername=" + emailHost);

	}

}
