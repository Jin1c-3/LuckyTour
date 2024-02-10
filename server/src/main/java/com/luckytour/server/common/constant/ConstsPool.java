package com.luckytour.server.common.constant;

/**
 * 常量池
 *
 * @author qing
 * @date Created in 2023/7/31 19:12
 */
public final class ConstsPool {
	/**
	 * 启用
	 */
	public static final int ENABLE = 1;
	/**
	 * 禁用
	 */
	public static final int DISABLE = 0;

	/**
	 * 页面
	 */
	public static final int PAGE = 1;

	/**
	 * 按钮
	 */
	public static final int BUTTON = 2;

	/**
	 * JWT 在 Redis 中保存的key前缀
	 */
	public static final String REDIS_JWT_KEY_PREFIX = "security:jwt:";

	/**
	 * 星号
	 */
	public static final String SYMBOL_ASTERISK = "*";

	/**
	 * 邮箱符号
	 */
	public static final String SYMBOL_EMAIL = "@";

	/**
	 * 默认当前页码
	 */
	public static final int PAGE_DEFAULT_CURRENT = 1;

	/**
	 * 默认每页条数
	 */
	public static final int PAGE_DEFAULT_SIZE = 10;

	/**
	 * 匿名用户 用户名
	 */
	public static final String ANONYMOUS_NAME = "匿名用户";

	/**
	 * 保存的key
	 */
	public static final String TOKEN_KEY = "X-token";

	/**
	 * 文件系统分隔符
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * 男
	 */
	public static final Byte MALE_INT = 1;

	/**
	 * 女
	 */
	public static final Byte FEMALE_INT = 2;

	/**
	 * 男
	 */
	public static final String MALE_STR = "男";

	/**
	 * 女
	 */
	public static final String FEMALE_STR = "女";

	/**
	 * AopLog字段的最大值限制数
	 */
	public static final int AOP_LOG_MAX_LENGTH = 100;

	/**
	 * 彩云天气可查询的最大天数
	 */
	public static final int CAIYUN_MAX_DAYS = 15;

	/**
	 * 关注
	 */
	public static final byte FOLLOWED = 1;

	/**
	 * 未关注
	 */
	public static final byte UNFOLLOWED = 0;

	/**
	 * 点赞
	 */
	public static final byte LIKED = 1;

	/**
	 * 未点赞
	 */
	public static final byte UNLIKED = 0;

	/**
	 * 收藏
	 */
	public static final byte FAVOR = 1;

	/**
	 * 未收藏
	 */
	public static final byte UNFAVOR = 0;

	/**
	 * 私有化构造方法
	 */
	private ConstsPool() {
	}

}
