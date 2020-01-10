package com.zrzhen.zetty.net;

/**
 * @author chenanlian
 *
 * socket连接状态枚举
 */

public enum SocketSessionStatus {
	/**
	 * 新建会话
	 */
	NEW,
	/**
	 * 已连接
	 */
	CONNECTED,
	/**
	 * 已销毁
	 */
	DESTROYED
}
