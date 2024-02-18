package com.luckytour.server.common.constant;

/**
 * @author qing
 * @date Created in 2024/2/10 10:56
 */
public final class Alert {

	/**
	 * 博客id为空的警告词
	 */
	public static final String BLOG_ID_IS_NULL = "博客id不能为空";

	/**
	 * 用户id为空的警告词
	 */
	public static final String USER_ID_IS_NULL = "用户id不能为空";

	/**
	 * 博客id不合法的警告词
	 */
	public static final String BLOG_ID_ILLIGAL = "博客id不合法";

	/**
	 * 计划id为空的警告词
	 */
	public static final String PLAN_ID_IS_NULL = "计划id不能为空";

	/**
	 * 博客标题格式错误的警告词
	 */
	public static final String BLOG_TITLE_FORMAT_ERROR = "博客标题格式错误";

	/**
	 * 博客标题为空的警告词
	 */
	public static final String BLOG_TITLE_IS_NULL = "博客标题不能为空";

	/**
	 * 邮箱或手机号输入错误的警告词
	 */
	public static final String PARAM_NOT_EMAIL_OR_PHONE = "邮箱或手机号输入错误";

	/**
	 * 博客内容为空的警告词
	 */
	public static final String BLOG_CONTENT_IS_NULL = "博客内容不能为空";

	/**
	 * 参数为空的警告词
	 */
	public static final String PARAM_IS_NULL = "参数不能为空";

	/**
	 * 私有化构造方法
	 */
	private Alert() {
	}
}
