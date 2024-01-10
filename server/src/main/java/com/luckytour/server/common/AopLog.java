package com.luckytour.server.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义日志实体
 *
 * @author qing
 * @date Created in 2023/8/4 15:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AopLog {
	// http方法 GET POST PUT DELETE PATCH
	private String httpMethod;
	// 类方法
	private String classMethod;
	// 请求参数
	private Object requestParams;
	// 返回参数
	private Object result;
	// 接口耗时
	private Long timeCost;
	// 线程id
	private String threadId;
	// 线程名称
	private String threadName;
	// ip
	private String ip;
	// url
	private String url;
	// 操作系统
	private String operatingSystem;
	// 浏览器
	private String browser;
	// user-agent
	private String userAgent;

}
