package com.luckytour.server.interceptor;

import com.luckytour.server.common.annotation.UserLoginRequired;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.service.UserService;
import com.luckytour.server.util.JwtUtil;
import com.luckytour.server.util.ServerResponseUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * @author qing
 * @date Created in 2023/8/1 19:36
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

	private boolean isLoginRequired(HandlerMethod method) {
		return Optional.ofNullable(method.getBeanType().getAnnotation(UserLoginRequired.class))
				.map(UserLoginRequired::required)
				.or(() -> Optional.ofNullable(method.getMethod().getAnnotation(UserLoginRequired.class))
						.map(UserLoginRequired::required))
				.orElse(false);
	}

	private boolean validateToken(String token, HttpServletRequest request, HttpServletResponse response) {
		// 校验token是否过期
		// 解析token，如果过期了会抛出异常
		// 如果和Redis中存储的token不一致，说明用户在其他地方登录了，会抛出未登录异常
		Claims claims = JwtUtil.parse(token);
		String uid = claims.getId();
		log.debug("uid: {}", uid);
		if (Boolean.TRUE.equals(claims.get("rememberMe", Boolean.class))) {
			JwtUtil.renew(request);
		}
		log.debug("token校验通过");
		return true;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		//拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
		if (RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
			log.debug("OPTIONS请求，放行");
			return true;
		}
		if (!(handler instanceof HandlerMethod method)) {
			log.debug("不是HandlerMethod类型，放行");
			return true;
		}
		if (!isLoginRequired(method)) {
			log.debug("不存在UserLoginRequired注解或required=false，放行");
			return true;
		}

		String token = request.getHeader(ConstsPool.TOKEN_KEY);
		if (StringUtils.isBlank(token)) {
			ServerResponseUtil.renderJson(response, ServerStatus.UNAUTHORIZED, null);
			return false;
		}
		return validateToken(token, request, response);
	}

}
