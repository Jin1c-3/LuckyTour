package com.luckytour.server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author qing
 * @date Created in 2024/1/16 21:13
 */
@Data
@Document(collection = "city_description")
public class CityDescription {

	@Id
	private String id;

	private String city;

	private String citycode;

	private String province;

	private String description;

	private List<String> photos;
}
