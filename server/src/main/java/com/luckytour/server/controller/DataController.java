package com.luckytour.server.controller;

import com.luckytour.server.entity.CityDescription;
import com.luckytour.server.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/16 21:21
 */
@RestController
@RequestMapping("/data")
@Tag(name = "数据接口")
@Slf4j
@CrossOrigin
@Validated
public class DataController {
	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping("/getCityDescription")
	@Operation(summary = "获取城市描述")
	public ApiResponse<List<CityDescription>> getCityDescription(@Valid @NotBlank(message = "城市不能为空") String character) {
		return ApiResponse.ofSuccess(mongoTemplate.find(new Query(Criteria.where("city").regex(character)), CityDescription.class));
	}
}
