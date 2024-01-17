package com.luckytour.server.payload;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/1/17 15:51
 */
@Data
public class GaodeResponse {
	private String status;
	private String info;
	private List<Map<String, String>> results;
}
