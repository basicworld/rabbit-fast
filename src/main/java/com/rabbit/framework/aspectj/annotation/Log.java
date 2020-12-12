package com.rabbit.framework.aspectj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rabbit.system.constant.LogConstants;

/**
 * 操作日志记录注解
 * 
 * @author wlfei
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
	/**
	 * 操作类型
	 * 
	 * @return
	 */
	public String operateType() default LogConstants.TYPE_OTHER;

	/**
	 * 是否保存请求的参数
	 */
	public boolean isSaveRequestData() default true;
}
