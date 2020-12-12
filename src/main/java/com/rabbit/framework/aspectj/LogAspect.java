package com.rabbit.framework.aspectj;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import com.alibaba.fastjson.JSON;
import com.rabbit.common.constant.ResultConstants;
import com.rabbit.common.util.ServletUtils;
import com.rabbit.common.util.spring.SpringUtils;
import com.rabbit.framework.aspectj.annotation.Log;
import com.rabbit.framework.manager.AsyncManager;
import com.rabbit.framework.manager.factory.AsyncFactory;
import com.rabbit.framework.security.domain.LoginUser;
import com.rabbit.framework.security.service.TokenService;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.monitor.domain.SysLog;

/**
 * 系统日志处理
 * 
 * @author wlfei
 *
 */
@Aspect
@Component
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	/**
	 * 配置织入点
	 */
	@Pointcut("@annotation(com.rabbit.framework.aspectj.annotation.Log)")
	public void logPointCut() {
	}

	/**
	 * 处理完请求后执行
	 *
	 * @param joinPoint 切点
	 */
	@AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
	public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
		handleLog(joinPoint, null, jsonResult);
	}

	/**
	 * 拦截异常操作
	 * 
	 * @param joinPoint 切点
	 * @param e         异常
	 */
	@AfterThrowing(value = "logPointCut()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
		handleLog(joinPoint, e, null);
	}

	/**
	 * 处理日志
	 * 
	 * @param joinPoint
	 * @param e
	 * @param jsonResult
	 */
	protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
		logger.debug("handleLog...");
		try {
			// 获取注解
			Log controllerLog = getAnnotationLog(joinPoint);
			if (null == controllerLog) {
				return;
			}
			// 获取登录用户
			LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
			// 配置要写入数据库的日志
			SysLog sysLog = new SysLog();

			sysLog.setUserId(loginUser.getUser().getId());
			sysLog.setUserName(loginUser.getUsername());
			sysLog.setOperTime(new Date());
			sysLog.setOperType(controllerLog.operateType());
			// 类名
			String className = joinPoint.getTarget().getClass().getName();
			// 方法名
			String methodName = joinPoint.getSignature().getName();
			sysLog.setOperContent(className + "." + methodName + "()");

			// 请求参数
			sysLog.setRequestMethod(ServletUtils.getRequest().getMethod());
			if (controllerLog.isSaveRequestData() == true) {
				setRequestValue(joinPoint, sysLog);
			}

			sysLog.setReturnMsg(JSON.toJSONString(jsonResult));

			if (jsonResult instanceof AjaxResult) {
				AjaxResult map = (AjaxResult) jsonResult;
				Integer code = (Integer) map.get("code");
				sysLog.setIsSuccess(ResultConstants.CODE_SUCCESS == code);
			}

			// 存储
			AsyncManager.me().execute(AsyncFactory.recordOperLog(sysLog));

		} catch (Exception e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
	}

	/**
	 * 是否存在注解，如果存在就获取
	 */
	private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (method != null) {
			return method.getAnnotation(Log.class);
		}
		return null;
	}

	/**
	 * 获取请求的参数，放到log中
	 * 
	 * @param operLog 操作日志
	 * @throws Exception 异常
	 */
	private void setRequestValue(JoinPoint joinPoint, SysLog operLog) throws Exception {
		String requestMethod = operLog.getRequestMethod();
		if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
			String params = argsArrayToString(joinPoint.getArgs());
			operLog.setOperArgs(params);
		} else {
			Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest()
					.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			operLog.setOperArgs(paramsMap.toString());
		}
	}

	/**
	 * 参数拼装
	 */
	private String argsArrayToString(Object[] paramsArray) {
		String params = "";
		if (paramsArray != null && paramsArray.length > 0) {
			for (int i = 0; i < paramsArray.length; i++) {
				if (!isFilterObject(paramsArray[i])) {
					Object jsonObj = JSON.toJSON(paramsArray[i]);
					params += jsonObj.toString() + " ";
				}
			}
		}
		return params.trim();
	}

	/**
	 * 判断是否需要过滤的对象。
	 * 
	 * @param o 对象信息。
	 * @return 如果是需要过滤的对象，则返回true；否则返回false。
	 */
	public boolean isFilterObject(final Object o) {
		return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
	}

}
