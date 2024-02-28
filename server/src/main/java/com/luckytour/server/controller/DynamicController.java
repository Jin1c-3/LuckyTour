package com.luckytour.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.common.BaseException;
import com.luckytour.server.common.annotation.UserLoginRequired;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.entity.Plan;
import com.luckytour.server.payload.front.MonitorRequest;
import com.luckytour.server.pojo.Position;
import com.luckytour.server.service.PlanService;
import com.luckytour.server.tasks.monitoruser.UserMonitorCache;
import com.luckytour.server.tasks.monitoruser.UserRealTimeInfo;
import com.luckytour.server.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 动态监视控制器
 *
 * @author qing
 * @date Created in 2024/2/16 17:00
 */
@RestController
@RequestMapping("/dynamic")
@Tag(name = "动态监视控制器")
@CrossOrigin
@Slf4j
@Validated
public class DynamicController {

	/*@PostConstruct
	public void init() {
		scheduledExecutorSupplier.startMonitoring();
	}*/

	/*private final DynamicCheckService dynamicCheckService;

	@Resource
	private final PushService jiguangPushService;

	private final UserService userService;

	private final ScheduledExecutorSupplier scheduledExecutorSupplier;*/

	private final UserMonitorCache userMonitorCache;

	private final PlanService planService;

	private final ObjectMapper objectMapper;


	@Autowired
	public DynamicController(UserMonitorCache userMonitorCache, PlanService planService, ObjectMapper objectMapper) {
		this.userMonitorCache = userMonitorCache;
		this.planService = planService;
		this.objectMapper = objectMapper;
	}

	private boolean validateMonitorTime(Set<String> planTimeSet) {
		LocalDate today = LocalDate.now();
		return planTimeSet.stream()
				.map(date -> LocalDate.parse(date, ConstsPool.DATE_FORMATTER))
				.anyMatch(date -> date.isEqual(today));
	}

	@PostMapping("/monitor")
	@Operation(summary = "监视用户状态")
	@Parameter(name = "latitudeAndLongitude", description = "经纬度的字符串，要有小数点和逗号", required = true, example = "122.183245,37.499276")
	@UserLoginRequired
	public Mono<ServerResponseEntity<String>> monitor(@Valid MonitorRequest monitorRequest, HttpServletRequest request) throws JsonProcessingException {
		String userId = JwtUtil.parseId(request);

		Optional<String> planContentOptional = planService.lambdaQuery()
				.eq(Plan::getUid, userId)
				.eq(Plan::getPid, monitorRequest.getPid())
				.select(Plan::getContent)
				.oneOpt()
				.map(Plan::getContent);

		if (planContentOptional.isEmpty()) {
			throw new BaseException(ServerStatus.PARAM_NOT_MATCH);
		}
		Map<String, List<Map<String, Object>>> planContentMap = objectMapper.readValue(planContentOptional.get(), new TypeReference<Map<String, List<Map<String, Object>>>>() {
		});
		boolean isLegalMonitorTime = validateMonitorTime(planContentMap.keySet());

		if (!isLegalMonitorTime) {
			return Mono.just(ServerResponseEntity.ofStatus(ServerStatus.ILLEGAL_MONITOR_TIME));
		}

		userMonitorCache.addOrUpdateUserMonitor(UserRealTimeInfo.builder()
				.userId(userId)
				.pid(monitorRequest.getPid())
				.position(Position.create(monitorRequest.getLatitudeAndLongitude()))
				.monitorCount(0)
				.build());
		return Mono.just(ServerResponseEntity.ofSuccess());
	}
}
