package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckytour.server.common.constant.CaiyunWeather;
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

import java.time.LocalDate;
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

	/**
	 * 计划验证
	 *
	 * @return true: 通过验证 prompt: 未通过验证返回响应提示词
	 */
	@PostMapping("/check")
	@Operation(summary = "计划验证")
	public ApiResponse<String> check(@RequestBody Map<String, List<Spot>> data) {
		// 拿出有日期且有经纬度的景点
		List<Map.Entry<Spot, String>> datedLocatedSpots = data.entrySet().stream()
				.flatMap(entry -> entry.getValue().stream()
						.filter(spot -> spot.getLocation() != null)
						.map(spot -> Map.entry(spot, entry.getKey())))
				.toList();

		// prompt用作直接返回给AI的提示词
		Optional<String> badWeatherPrompt = datedLocatedSpots.stream()
				.map(entry -> {
					String weather = ApiRequestUtil.getWeather(entry.getKey().getLocation(), entry.getValue());
					String caiyunWeatherExplanation = CaiyunWeather.WEATHER_MAP.get(weather);
					return Judgment.GOOD_CAIYUN_WEATHER.stream()
							.noneMatch(caiyunWeatherExplanation::equals)
							? Judgment.getBadWeatherPrompt(String.valueOf(LocalDate.parse(entry.getValue()).getDayOfMonth()), entry.getKey().getCityname() + entry.getKey().getName(), caiyunWeatherExplanation)
							: null;
				})
				.filter(Objects::nonNull)
				.findFirst();
		if (badWeatherPrompt.isPresent()) {
			return ApiResponse.ofSuccess(badWeatherPrompt.get());
		}
		Optional<String> tooFarPrompt = IntStream.range(0, datedLocatedSpots.size() - 1)
				.mapToObj(i -> {
					double distance = ApiRequestUtil.getStraightDistance(List.of(datedLocatedSpots.get(i).getKey().getLocation()), datedLocatedSpots.get(i + 1).getKey().getLocation()).get(0);
					return distance > Judgment.STRAIGHT_DISTANCE_TOO_FAR
							? Judgment.getTooFarPrompt(datedLocatedSpots.get(i).getKey().getName(), datedLocatedSpots.get(i + 1).getKey().getName())
							: null;
				})
				.filter(Objects::nonNull)
				.findFirst();
		return tooFarPrompt.map(ApiResponse::ofSuccess).orElseGet(() -> ApiResponse.ofSuccess("true"));
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
