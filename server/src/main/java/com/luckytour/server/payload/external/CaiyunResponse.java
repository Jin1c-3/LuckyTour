package com.luckytour.server.payload.external;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/31 23:09
 */
@Data
public class CaiyunResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	public static class Result implements Serializable {
		@Serial
		private static final long serialVersionUID = 2L;

		@Getter
		@Setter
		public static class ForecastWay implements Serializable {
			@Serial
			private static final long serialVersionUID = 3L;

			@Getter
			@Setter
			public static class TimeValue implements Serializable {
				@Serial
				private static final long serialVersionUID = 4L;

				private String date;
				private String value;
			}

			private String status;
			private List<TimeValue> skycon;
		}

		private ForecastWay daily;
		private ForecastWay hourly;
	}

	private String status;
	private Result result;
}
