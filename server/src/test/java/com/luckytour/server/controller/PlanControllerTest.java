package com.luckytour.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckytour.server.payload.LoginRequest;
import com.luckytour.server.payload.PlanCreateRequest;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author qing
 * @date Created in 2024/2/7 19:43
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PlanControllerTest {

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
		Map<String, List<Object>> planMap = new HashMap<>();
		planMap.put("2024-01-21", Arrays.asList(
				new HashMap<String, String>() {{
					put("name", "泸州长江大桥");
					put("type", "景点");
				}},
				new HashMap<String, String>() {{
					put("name", "四川省泸州市泸州老窖集团");
					put("type", "景点");
				}},
				new HashMap<String, String>() {{
					put("name", "蜀南竹海");
					put("type", "景点");
				}}
		));
		planMap.put("2024-01-22", Arrays.asList(
				new HashMap<String, String>() {{
					put("name", "泸州白塔公园");
					put("type", "景点");
				}},
				new HashMap<String, String>() {{
					put("name", "江阳区龙马潭");
					put("type", "景点");
				}},
				new HashMap<String, String>() {{
					put("name", "泸州古蔺沱江戏水公园");
					put("type", "景点");
				}}
		));

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
				.andExpect(status().isOk());
	}
}
