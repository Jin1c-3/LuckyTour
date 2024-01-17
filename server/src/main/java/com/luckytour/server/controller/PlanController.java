package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.Judgment;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.payload.ApiResponse;
import com.luckytour.server.payload.Spot;
import com.luckytour.server.service.PlanService;
import com.luckytour.server.util.ApiRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * @author qing
 * @date Created in 2024/1/16 9:11
 */
@RestController
@RequestMapping("/plan")
@Tag(name = "计划接口")
@Slf4j
@CrossOrigin
@Validated

public class PlanController {
	@Autowired
	private PlanService planService;

	@PostMapping("/check")
	@Operation(summary = "计划验证")
	public ApiResponse<String> check(@RequestBody Map<String, List<Spot>> data) {
		List<Spot> spots = new ArrayList<>();
		for (Map.Entry<String, List<Spot>> entry : data.entrySet()) {
			spots.addAll(entry.getValue());
		}
		log.debug("spots: {}", spots);
		List<Spot> notNullSpots = spots.stream().filter(spot -> spot.getLocation() != null).toList();
		AtomicBoolean result = new AtomicBoolean(true);
		AtomicReference<String> prompt = new AtomicReference<>("");
		IntStream.range(0, spots.size() - 1)
				.anyMatch(i -> {
					if (ApiRequestUtil.getStraightDistance(notNullSpots.get(i).getLocation(), notNullSpots.get(i + 1).getLocation()).get(0)
							> Judgment.STRAIGHT_DISTANCE_TOO_FAR) {
						result.set(false);
						prompt.set(String.format(Judgment.STRAIGHT_DISTANCE_TOO_FAR_PROMPT, notNullSpots.get(i).getName(), notNullSpots.get(i + 1).getName()));
						return true;
					}
					return false;
				});
		if(result.get()) {
			return ApiResponse.ofSuccess("true");
		}
		return ApiResponse.ofSuccess(prompt.get());
	}

	@PostMapping("/saveOrUpdate")
	@Operation(summary = "计划存储更新")
	public boolean saveOrUpdate(@Valid @RequestBody Plan plan) throws MysqlException {
		planService.saveOrUpdateByMultiId(plan);
		return true;
	}

	@GetMapping("/getByUid")
	@Operation(summary = "通过uid获取计划")
	public ApiResponse<List<Plan>> getByUid(@Valid @NotBlank(message = "uid不能为空") String uid) throws MysqlException {
		if (planService.list(new QueryWrapper<Plan>().eq("uid", uid)).isEmpty()) {
			return ApiResponse.ofSuccessMsg("暂时没有计划哟~");
		}
		return ApiResponse.ofSuccess(planService.list(new QueryWrapper<Plan>().eq("uid", uid)));
	}
}
