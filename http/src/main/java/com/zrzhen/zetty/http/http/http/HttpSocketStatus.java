package com.zrzhen.zetty.http.http.http;

/**
 * @author chenanlian
 *
 * socket状态枚举
 */

public enum HttpSocketStatus {
	/**
	 * 跳过无效字节
	 */
	SKIP_CONTROL_CHARS,
	/**
	 * 读取请求行
	 */
	READ_INITIAL,
	/**
	 * 读取请求头
	 */
	READ_HEADER,
	/**
	 * 读取请求body
	 */
	READ_VARIABLE_LENGTH_CONTENT,
	/**
	 * 请求消息读取完成
	 */
	READ_REQUEST_FINISH,

	/**
	 * 处理中
	 */
	PROCESSING,
	/**
	 * 写
	 */
	WRITING;
}
