package com.luckytour.server.payload.front;

import com.luckytour.server.common.constant.Alert;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qing
 * @date Created in 2024/2/21 20:56
 */
@Schema(name = "MonitorRequest", description = "动态监控请求类")
@Getter
@Setter
public class MonitorRequest implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "经纬度", example = "30.123456,120.123456")
	@NotBlank(message = Alert.PARAM_IS_NULL)
	private String latitudeAndLongitude;

	@Schema(description = "计划id")
	@NotBlank(message = Alert.PARAM_IS_NULL)
	private String pid;
}
