package com.luckytour.server.payload.front;

import com.baomidou.mybatisplus.annotation.TableField;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qing
 * @date Created in 2024/3/13 21:01
 */
@Getter
@Setter
public class PlanSaveRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String uid;

	private String pid;

	private String content;

	private String title;

	private String tags;

	private String city;

}
