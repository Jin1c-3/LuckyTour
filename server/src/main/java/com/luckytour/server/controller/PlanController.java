package com.luckytour.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.payload.front.PlanCreateRequest;
import com.luckytour.server.pojo.Spot;
import com.luckytour.server.service.PlanService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.PlanVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 计划控制器
 *
 * @author qing
 * @date Created in 2024/1/16 9:11
 */
@RestController
@RequestMapping("/plan")
@Tag(name = "计划控制器")
@Slf4j
@CrossOrigin
@Validated
public class PlanController {

	private final PlanService planService;

	private final ObjectMapper objectMapper;

	@Autowired
	public PlanController(PlanService planService, ObjectMapper objectMapper) {
		this.planService = planService;
		this.objectMapper = objectMapper;
	}

	@PostMapping("/check")
	@Operation(summary = "计划验证，用于自由模式计划")
	public Mono<ServerResponseEntity<String>> check(@RequestBody Map<String, List<Spot>> data) {
		// 拿出有日期且有经纬度的景点
		List<Map.Entry<Spot, String>> datedLocatedSpots = data.entrySet().stream()
				.flatMap(entry -> entry.getValue().stream()
						.filter(spot -> spot.getLocation() != null)
						.map(spot -> Map.entry(spot, entry.getKey())))
				.toList();

		// prompt用作直接返回给AI的提示词
		return planService.check(datedLocatedSpots)
				.map(ServerResponseEntity::ofSuccess);
	}

	@PostMapping("/saveOrUpdate")
	@Operation(summary = "计划存储更新，用于自由模式计划")
	public boolean saveOrUpdate(@Valid @RequestBody Plan plan) throws MysqlException {
		return planService.saveOrUpdateByMultiId(plan);
	}

	@GetMapping("/getByUid")
	@Operation(summary = "用户uid获取计划")
	public ServerResponseEntity<List<Plan>> getByUid(@Valid @NotBlank(message = "uid不能为空") String uid) throws MysqlException {
		if (planService.list(new QueryWrapper<Plan>().eq("uid", uid)).isEmpty()) {
			return ServerResponseEntity.ofSuccessMsg("暂时没有计划哟~");
		}
		return ServerResponseEntity.ofSuccess(planService.list(new QueryWrapper<Plan>().eq("uid", uid)));
	}

	@GetMapping("/getByRequest")
	@Operation(summary = "用户uid获取计划")
	public ServerResponseEntity<List<Plan>> getByRequest(HttpServletRequest request) throws MysqlException {
		return getByUid(JwtUtil.parseId(request));
	}

	@PostMapping("/create")
	@Operation(summary = "生成标签模式的旅行计划")
	public Mono<ServerResponseEntity<PlanVO>> create(@Valid @RequestBody PlanCreateRequest planCreateRequest) throws MysqlException {
		// 这里没有错误处理，没有考虑计划生成问题，认为一定有计划返回
		// 没有返回认为会报错，代码不会执行到这里
		Plan plan = Plan.create(planCreateRequest);
		Map<String, Object> prompt = planCreateRequest.getPrompt();
		prompt.put("uid", planCreateRequest.getUid());
		return planService.create(prompt).doOnNext(monoPlan -> {
					try {
						plan.setContent(objectMapper.writeValueAsString(monoPlan));
					} catch (JsonProcessingException e) {
						log.debug("生成标签模式的旅行计划时，序列化失败", e);
						plan.setContent(monoPlan.toString());
					}
					planService.saveOrUpdateByMultiId(plan);
				}).map(monoPlan -> {
					PlanVO planVO = PlanVO.create(plan);
					planVO.setSpots(monoPlan);
					return planVO;
				})
				.map(ServerResponseEntity::ofSuccess);
	}
}
