package com.luckytour.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.payload.front.LoginRequest;
import com.luckytour.server.payload.front.PlanCreateRequest;
import com.luckytour.server.service.GptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author qing
 * @date Created in 2024/2/7 19:43
 */
@SpringBootTest
@AutoConfigureMockMvc
class PlanControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private GptService gptService;

	private String loginAndReturnToken() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmailOrPhone("test@qq.com");
		loginRequest.setPassword("123456");

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andReturn();

		String response = result.getResponse().getContentAsString();
		JsonNode responseJson = objectMapper.readTree(response);
		return responseJson.get("data").get("token").asText();
	}

	@Test
	void testCreate() throws Exception {
		String jsonString="{\"1994-04-01\":[{\"address\":\"高区新浪屿花园15号楼\",\"business\":{\"keytag\":\"民宿\",\"rating\":\"4.1\",\"tel\":\"+861056320612,5417462\",\"rectag\":\"民宿\"},\"pcode\":\"370000\",\"adcode\":\"371002\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"住宿服务;住宿服务相关;住宿服务相关\",\"photos\":[{\"title\":\"Logo\",\"url\":\"http://store.is.autonavi.com/showpic/b447275ed16d4d116c4536cd8f3fddaf\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/0722fd4e5073f390cb78207de7576109\"},{\"title\":\"客房\",\"url\":\"http://store.is.autonavi.com/showpic/ab7b34d1f7518eb3374a87fed9ca7396\"}],\"typecode\":\"100000\",\"adname\":\"环翠区\",\"citycode\":\"0631\",\"name\":\"威海观海民宿\",\"location\":\"122.068362,37.532296\",\"交通方式\":\"步行\"},{\"address\":\"滨海大道1号\",\"business\":{\"opentime_today\":\"08:00-19:00\",\"keytag\":\"风景名胜,风景名胜相关,旅游景点\",\"rating\":\"3.9\",\"tel\":\"0631-7857777\",\"opentime_week\":\"08:00-19:00\"},\"pcode\":\"370000\",\"adcode\":\"371082\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"风景名胜;风景名胜相关;旅游景点\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/6d7913931f9e4aaa9ebf21be1d4eb242\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/2a80d817a934ccd61d0625b33a505cfe\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/6d868386293f65ba338a88ed4744a74e\"}],\"typecode\":\"110000\",\"adname\":\"荣成市\",\"citycode\":\"0631\",\"name\":\"金石湾艺术园区\",\"location\":\"122.611164,37.418829\",\"交通方式\":\"步行\"},{\"address\":\"华夏路1号\",\"business\":{\"opentime_today\":\"08:00-17:30\",\"cost\":\"98.00\",\"keytag\":\"5A景区\",\"rating\":\"4.4\",\"tel\":\"0631-5999150;0631-5999136\",\"rectag\":\"体现东方古典文化\",\"opentime_week\":\"08:00-17:30\"},\"pcode\":\"370000\",\"adcode\":\"371002\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"风景名胜;风景名胜;国家级景点\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/bf5728fc4eb7de8a0bf021ea80c63bae\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/efaa94016f7230c76b43fb3d6956186e\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/1a685843483b1d5a4e8d14292ed73cd7\"}],\"typecode\":\"110202\",\"adname\":\"环翠区\",\"citycode\":\"0631\",\"name\":\"威海华夏城旅游风景区\",\"location\":\"122.115425,37.424765\",\"交通方式\":\"步行\"},{\"address\":\"丽食盛世家宴海滨中路29号\",\"business\":{\"opentime_today\":\"24小时营业\",\"cost\":\"0.00\",\"keytag\":\"公园\",\"rating\":\"5.0\",\"tag\":\"螃蟹\",\"rectag\":\"春节亲子\",\"opentime_week\":\"00:00-24:00\"},\"pcode\":\"370000\",\"adcode\":\"371002\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"风景名胜;公园广场;公园\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/24fd9a03839c8247bb580de2157c0f80\"},{\"title\":\"威海公园\",\"url\":\"http://store.is.autonavi.com/showpic/11c6ba34a7bde80f078e17bf484c95dc\"},{\"title\":\"威海公园\",\"url\":\"http://store.is.autonavi.com/showpic/2904929782548f0e36d7b81564fa623c\"}],\"typecode\":\"110101\",\"adname\":\"环翠区\",\"citycode\":\"0631\",\"name\":\"威海公园\",\"location\":\"122.144823,37.465201\",\"交通方式\":\"步行\"},{\"address\":\"环海路6699号(威海经济开发区与荣成交汇处)\",\"business\":{\"opentime_today\":\"24小时营业\",\"cost\":\"0.00\",\"keytag\":\"4A景区\",\"rating\":\"4.9\",\"tel\":\"0631-7866666;0631-7861666\",\"tag\":\"蘑菇,特色小吃,螃蟹,coffee\",\"rectag\":\"春节周边\",\"opentime_week\":\"00:00-24:00\"},\"pcode\":\"370000\",\"adcode\":\"371082\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"风景名胜;风景名胜;国家级景点\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/3e5c17efe73f6f55adc1352b45746b8c\"},{\"title\":\"\",\"url\":\"https://aos-comment.amap.com/B027716LHP/headerImg/1a9ce98fad3bd3eee1a6a01eefd27e3d_2048_2048_80.jpg\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/80a4a4af552a58f6ac580c49fe2132ba\"}],\"typecode\":\"110202\",\"adname\":\"荣成市\",\"citycode\":\"0631\",\"name\":\"那香海景区\",\"location\":\"122.415268,37.404053\",\"交通方式\":\"步行\"},{\"address\":\"西霞口村\",\"business\":{\"opentime_today\":\"08:00-16:30\",\"cost\":\"180.00\",\"keytag\":\"4A景区\",\"rating\":\"4.6\",\"alias\":\"成山头景区\",\"tel\":\"0631-7834888;0631-7834666\",\"rectag\":\"春节亲子\",\"opentime_week\":\"08:00-16:30\"},\"pcode\":\"370000\",\"adcode\":\"371082\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"风景名胜;风景名胜;国家级景点\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/d6de024e879d9c6603b33c1c900734e7\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/73ad43fb15511b9df993c480071c63ff\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/8436ff57ef646a0f159629e500ef56ff\"}],\"typecode\":\"110202\",\"adname\":\"荣成市\",\"citycode\":\"0631\",\"name\":\"成山头国家重点风景名胜区\",\"location\":\"122.698982,37.401724\",\"交通方式\":\"步行\"}],\"1994-04-02\":[{\"address\":\"北环海路178号\",\"business\":{\"opentime_today\":\"09:00-18:00\",\"cost\":\"87.00\",\"keytag\":\"景区\",\"rating\":\"5.0\",\"tel\":\"0631-5623885\",\"tag\":\"大排档,鲅鱼水饺,小吃街,螃蟹,海鲜大排档\",\"rectag\":\"景区\",\"opentime_week\":\"09:00-18:00\"},\"pcode\":\"370000\",\"adcode\":\"371002\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"风景名胜;风景名胜;海滩\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/125caef497aceef3146494fc1d93371b\"},{\"title\":\"威海国际海水浴场\",\"url\":\"http://store.is.autonavi.com/showpic/011c8496e3f71e9efa1a79b7f2fb9c4b\"},{\"title\":\"威海国际海水浴场\",\"url\":\"http://store.is.autonavi.com/showpic/78ba8eedf4dd16f0b95aa43c5964d34f\"}],\"typecode\":\"110208\",\"adname\":\"环翠区\",\"citycode\":\"0631\",\"name\":\"威海国际海水浴场\",\"location\":\"122.042007,37.527536\",\"交通方式\":\"步行\"},{\"address\":\"环翠区\",\"business\":{\"keytag\":\"岛屿\",\"rectag\":\"岛屿\"},\"pcode\":\"370000\",\"adcode\":\"371002\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"地名地址信息;自然地名;岛屿\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/e41ff69f4f643401f07e8f6ab661518e\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/f90259fcbb6f10c56d3555776afcafb8\"},{\"title\":\"\",\"url\":\"http://s-pic.oss-cn-beijing.aliyuncs.com/desensitize/images/publish/102d8f19ad49a1b778767dc1ca21cb84.jpg\"}],\"typecode\":\"190202\",\"adname\":\"环翠区\",\"citycode\":\"0631\",\"name\":\"刘公岛\",\"location\":\"122.20468,37.499906\",\"交通方式\":\"步行\"},{\"address\":\"环翠楼街道幸福公园海滨大厦东-威海幸福门幸福门租车处\",\"business\":{\"opentime_today\":\"24小时营业\",\"cost\":\"88.00\",\"keytag\":\"广场\",\"rating\":\"4.9\",\"business_area\":\"幸福广场\",\"tel\":\"0631-5555288\",\"tag\":\"酸梅汤\",\"rectag\":\"广场\",\"opentime_week\":\"每天00:00-24:00\"},\"pcode\":\"370000\",\"adcode\":\"371002\",\"pname\":\"山东省\",\"cityname\":\"威海市\",\"type\":\"风景名胜;公园广场;公园广场\",\"photos\":[{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/acc5f8e52f3f9d6349951d39b4f82671\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/92417e5d0ef6a3f504208ada4464bb55\"},{\"title\":\"\",\"url\":\"http://store.is.autonavi.com/showpic/8bfb2a1f130100f75cadab30d6faeb81\"}],\"typecode\":\"110100\",\"adname\":\"环翠区\",\"citycode\":\"0631\",\"name\":\"幸福门广场\",\"location\":\"122.127895,37.501526\",\"交通方式\":\"步行\"}]}";
		Map<String, List<Object>> planMap = objectMapper.readValue(jsonString, new TypeReference<>() {});

		PlanCreateRequest planCreateRequest = new PlanCreateRequest();
		planCreateRequest.setPrompt(new HashMap<>());
		planCreateRequest.setCity("泸州");
		planCreateRequest.setTags(List.of("景点"));
		planCreateRequest.setTitle("泸州两日游");
		planCreateRequest.setUid("c2283170-f41d-4636-b15b-8644d65883c6");

		when(gptService.createPlan(any())).thenReturn(Mono.just(planMap));

		mockMvc.perform(MockMvcRequestBuilders.post("/plan/create")
						.contentType(MediaType.APPLICATION_JSON)
						.header("X-token", loginAndReturnToken())
						.content(objectMapper.writeValueAsString(planCreateRequest)))
				.andReturn();
	}
}
