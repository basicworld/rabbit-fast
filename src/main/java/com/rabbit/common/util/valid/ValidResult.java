package com.rabbit.common.util.valid;

import com.rabbit.common.constant.BaseConstants;

/**
 * bean校验结果类
 * 
 * @author wlfei
 *
 */
public class ValidResult {
	/**
	 * 结果标记 0--无错误 1--有错误
	 */
	private Integer errorTag;
	/**
	 * 结果描述
	 */
	private String message;

	/**
	 * 成功返回
	 * 
	 * @param message
	 * @return
	 */
	public static ValidResult success(String message) {
		ValidResult result = new ValidResult();
		result.setErrorTag(BaseConstants.NO);
		result.setMessage(message);
		return result;
	}

	/**
	 * 成功返回
	 * 
	 * @param message
	 * @return
	 */
	public static ValidResult success() {
		ValidResult result = new ValidResult();
		result.setErrorTag(BaseConstants.NO);
		result.setMessage("success");
		return result;
	}

	/**
	 * 错误返回
	 * 
	 * @param message
	 * @return
	 */
	public static ValidResult error(String message) {
		ValidResult result = new ValidResult();
		result.setErrorTag(BaseConstants.YES);
		result.setMessage(message);
		return result;
	}

	/**
	 * 判断是否有错误
	 * 
	 * @return true--有错误 false--没有错误
	 */
	public boolean hasError() {
		if (BaseConstants.YES == this.errorTag) {
			return true;
		}
		return false;
	}

	private void setErrorTag(Integer errorTag) {
		this.errorTag = errorTag;
	}

	/**
	 * 返回校验信息
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

}
