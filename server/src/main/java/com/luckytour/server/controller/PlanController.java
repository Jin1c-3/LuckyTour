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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
		List<Spot> spots = data.values().stream()
				.flatMap(List::stream)
				.toList();
		log.debug("spots: {}", spots);
		List<Spot> notNullSpots = spots.stream()
				.filter(spot -> spot.getLocation() != null)
				.toList();
		Optional<String> prompt = IntStream.range(0, notNullSpots.size() - 1)
				.mapToObj(i -> {
					if (ApiRequestUtil.getStraightDistance(List.of(notNullSpots.get(i).getLocation()), notNullSpots.get(i + 1).getLocation()).get(0)
							> Judgment.STRAIGHT_DISTANCE_TOO_FAR) {
						return String.format(Judgment.STRAIGHT_DISTANCE_TOO_FAR_PROMPT, notNullSpots.get(i).getName(), notNullSpots.get(i + 1).getName());
					}
					return null;
				})
				.filter(Objects::nonNull)
				.findFirst();
		return ApiResponse.ofSuccess(prompt.orElse("true"));
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
