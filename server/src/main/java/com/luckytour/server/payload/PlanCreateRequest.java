package com.luckytour.server.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/2/7 16:18
 */
@Getter
@Setter
@Schema(name = "PlanCreateRequest", description = "计划生成请求")
public class PlanCreateRequest {

	@Schema(description = "user表中的id列")
	@NotBlank(message = "用户id不能为空")
	private String uid;

	@Schema(description = "传递给flask的AI提示词类")
	private Map<String, Object> prompt;

	@Schema(description = "计划标题，nvarchar(255)")
	@NotBlank(message = "计划标题不能为空")
	private String title;

	@Schema(description = "标签数组")
	private List<String> tags;

	@Schema(description = "计划所处城市，nvarchar(10)")
	@NotBlank(message = "城市不能为空")
	private String city;
}
