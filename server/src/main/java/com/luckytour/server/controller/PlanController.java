package com.luckytour.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.luckytour.server.common.annotation.UserLoginRequired;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.exception.MysqlException;
import com.luckytour.server.payload.front.PlanCreateRequest;
import com.luckytour.server.payload.front.PlanSaveRequest;
import com.luckytour.server.pojo.Spot;
import com.luckytour.server.service.PlanService;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.vo.PlanVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
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

	private final UserService userService;

	@Autowired
	public PlanController(PlanService planService, ObjectMapper objectMapper, UserService userService) {
		this.planService = planService;
		this.userService = userService;
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ConstsPool.DATE_TIME_FORMATTER));
		objectMapper.registerModule(javaTimeModule);
		this.objectMapper = objectMapper;

	}

	@PostMapping("/check")
	@Operation(summary = "计划验证，用于自由模式计划")
	@UserLoginRequired(required = false)
	@Parameter(name = "data", description = "日期和景点列表", required = true)
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
	@UserLoginRequired(required = false)
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "计划", required = true, content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Plan.class)))
	public boolean saveOrUpdate(@Valid @RequestBody PlanSaveRequest plan) throws MysqlException {
		return planService.saveOrUpdateByMultiId(Plan.create(plan));
	}

	@GetMapping("/getByUid")
	@Operation(summary = "用户uid获取计划")
	@Parameter(name = "uid", description = "用户id", required = true)
	public ServerResponseEntity<List<Plan>> getByUid(@NotBlank(message = "uid不能为空") String uid) throws MysqlException {
		if (!userService.idIsExist(uid)) {
			return ServerResponseEntity.ofStatus(ServerStatus.USER_NOT_EXIST);
		}
		var planList = planService.lambdaQuery().eq(Plan::getUid, uid).list();
		return planList.isEmpty()
				? ServerResponseEntity.ofStatus(ServerStatus.USER_HAS_NO_PLAN)
				: ServerResponseEntity.ofSuccess(planList);
	}

	@GetMapping("/getByPid")
	@Operation(summary = "计划pid获取计划")
	@Parameter(name = "pid", description = "计划pid", required = true)
	public ServerResponseEntity<Plan> getByPid(@NotBlank(message = "pid不能为空") String pid) throws MysqlException {
		var plan = planService.lambdaQuery().eq(Plan::getPid, LocalDateTime.parse(pid, ConstsPool.DATE_TIME_FORMATTER)).oneOpt();
		return plan.map(ServerResponseEntity::ofSuccess)
				.orElseGet(() -> ServerResponseEntity.ofStatus(ServerStatus.PLAN_NOT_EXIST));
	}

	@GetMapping("/getByRequest")
	@Operation(summary = "用户uid获取计划")
	@UserLoginRequired
	public ServerResponseEntity<List<Plan>> getByRequest(HttpServletRequest request) throws MysqlException {
		var planList = planService.lambdaQuery().eq(Plan::getUid, JwtUtil.parseId(request)).list();
		return planList.isEmpty()
				? ServerResponseEntity.ofStatus(ServerStatus.USER_HAS_NO_PLAN)
				: ServerResponseEntity.ofSuccess(planList);
	}

	@GetMapping("/delete")
	@Operation(summary = "删除计划")
	@Parameter(name = "pid", description = "计划pid", required = true)
	public ServerResponseEntity<String> delete(@NotBlank(message = "pid不能为空") String pid, HttpServletRequest request) throws MysqlException {
		String uid = JwtUtil.parseId(request);
		return planService.lambdaUpdate().eq(Plan::getPid, LocalDateTime.parse(pid)).eq(Plan::getUid, uid).remove()
				? ServerResponseEntity.ofSuccess("删除成功")
				: ServerResponseEntity.ofStatus(ServerStatus.PLAN_NOT_EXIST);
	}

	@PostMapping("/create")
	@Operation(summary = "生成标签模式的旅行计划")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "计划生成请求", required = true, content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PlanCreateRequest.class)))
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
