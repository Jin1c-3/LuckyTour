package com.luckytour.server.payload.external;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author qing
 * @date Created in 2024/1/31 21:55
 */
@Data
public class GaodeResponse {
	private String status;
	private String info;
	private List<Map<String, String>> results;
	private List<Map<String, Object>> geocodes;
}