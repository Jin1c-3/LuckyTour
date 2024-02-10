package com.luckytour.server.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 分页请求参数
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-12 18:05
 */
@Data
@Schema(name = "PageCondition", description = "分页请求参数")
public class PageCondition implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "当前页码，从1开始")
	private Integer currentPage;

	@Schema(description = "每页显示数量")
	private Integer pageSize;

}
