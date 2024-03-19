package com.luckytour.server.controller;

import com.luckytour.server.common.constant.Regex;
import com.luckytour.server.entity.CityDescription;
import com.luckytour.server.common.http.ServerResponseEntity;
import com.luckytour.server.service.GeographicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 数据控制器
 *
 * @author qing
 * @date Created in 2024/1/16 21:21
 */
@RestController
@RequestMapping("/data")
@Tag(name = "数据控制器")
@Slf4j
@CrossOrigin
@Validated
public class DataController {

	private final MongoTemplate mongoTemplate;

	@Resource
	private final GeographicService geographicService;

	@Autowired
	public DataController(MongoTemplate mongoTemplate, GeographicService geographicService) {
		this.mongoTemplate = mongoTemplate;
		this.geographicService = geographicService;
	}

	@GetMapping("/getCityDescription")
	@Operation(summary = "获取城市描述")
	@Parameter(name = "character", description = "城市名称的首字母或汉字")
	public ServerResponseEntity<List<CityDescription>> getCityDescription(/*@Valid @NotBlank(message = "城市不能为空") */String character) {
		// 保护机制
		if (!character.matches(Regex.CHINESE_REGEX)) {
			return ServerResponseEntity.ofSuccess();
		}
		return ServerResponseEntity.ofSuccess(mongoTemplate.find(new Query(Criteria.where("city").regex(character)), CityDescription.class));
	}

	@GetMapping("/getGeoCode")
	@Operation(summary = "获取地址的经纬度")
	@Parameter(name = "address", description = "地址")
	public Mono<ServerResponseEntity<String>> getGeoCode(@Valid @NotBlank(message = "地址不能为空") String address) {
		return geographicService.getLongitudeAndLatitude(address)
				.map(ServerResponseEntity::ofSuccess);
	}

}
