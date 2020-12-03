package com.rabbit.system.domain;

/**
 * 验证码
 * 
 * @author wlfei
 *
 */
public class Captcha {
	/**
	 * 原始信息
	 */
	private String code;
	/**
	 * base64编码的图片信息
	 */
	private String base64;
	/**
	 * 唯一ID
	 */
	private String uuid;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBase64() {
		return base64;
	}

	public String getUuid() {
		return uuid;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
