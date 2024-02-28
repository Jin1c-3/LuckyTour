package com.luckytour.server.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author qing
 * @date Created in 2024/2/27 21:23
 */
@Getter
@Setter
public class BlogContent implements Serializable {

	@Serial
	private static final long serialVersionUID = 11L;

	private String destination;

	private String address;

	private String location;

	private String comment;

	private List<String> photos;
}