package com.luckytour.server.common.constant;

/**
 * 常量池
 *
 * @author qing
 * @date Created in 2023/7/31 19:12
 */
public interface Consts {
	/**
	 * 启用
	 */
	Integer ENABLE = 1;
	/**
	 * 禁用
	 */
	Integer DISABLE = 0;

	/**
	 * 页面
	 */
	Integer PAGE = 1;

	/**
	 * 按钮
	 */
	Integer BUTTON = 2;

	/**
	 * JWT 在 Redis 中保存的key前缀
	 */
	String REDIS_JWT_KEY_PREFIX = "security:jwt:";

	/**
	 * 星号
	 */
	String SYMBOL_ASTERISK = "*";

	/**
	 * 邮箱符号
	 */
	String SYMBOL_EMAIL = "@";

	/**
	 * 默认当前页码
	 */
	Integer PAGE_DEFAULT_CURRENT = 1;

	/**
	 * 默认每页条数
	 */
	Integer PAGE_DEFAULT_SIZE = 10;

	/**
	 * 匿名用户 用户名
	 */
	String ANONYMOUS_NAME = "匿名用户";

	/**
	 * 保存的key
	 */
	String TOKEN_KEY = "X-token";

	/**
	 * 文件系统分隔符
	 */
	String FILE_SEPARATOR = System.getProperty("file.separator");
}
