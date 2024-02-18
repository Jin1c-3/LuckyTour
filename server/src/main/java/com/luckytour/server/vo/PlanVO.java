package com.luckytour.server.vo;

import com.luckytour.server.entity.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/2/17 18:07
 */
@Getter
@Setter
public class PlanVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "user表中的id列")
	private String uid;

	@Schema(description = "计划创建的时间戳，双主键之一")
	private LocalDateTime pid;

	@Schema(description = "计划标题，nvarchar(255)")
	private String title;

	@Schema(description = "计划所处城市，nvarchar(10)")
	private String city;

	@Schema(description = "计划")
	private Map<String, List<Object>> spots;

	public static PlanVO create(Plan plan) {
		PlanVO planVO = new PlanVO();
		planVO.setUid(plan.getUid());
		planVO.setPid(plan.getPid());
		planVO.setTitle(plan.getTitle());
		planVO.setCity(plan.getCity());
		return planVO;
	}

}
