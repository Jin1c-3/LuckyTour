package com.luckytour.server.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/31 23:09
 */
@Data
public class CaiyunResponse {
	@Getter
	@Setter
	public static class Result {
		@Getter
		@Setter
		public static class Daily {
			@Getter
			@Setter
			public static class Skycon {
				private String date;
				private String value;
			}

			private String status;
			private List<Skycon> skycon;
		}

		private Daily daily;
	}

	private String status;
	private Result result;
}
