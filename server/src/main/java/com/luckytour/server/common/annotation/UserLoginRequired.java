package com.luckytour.server.common.annotation;

import java.lang.annotation.*;

/**
 * @author qing
 * @date Created in 2024/2/23 16:00
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UserLoginRequired {
	boolean required() default true;
}
