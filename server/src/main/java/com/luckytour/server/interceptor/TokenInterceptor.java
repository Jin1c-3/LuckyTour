package com.luckytour.server.interceptor;

import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.util.ApiResponseUtil;
import com.luckytour.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author qing
 * @date Created in 2023/8/1 19:36
 */
@Configuration
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		//拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
		if (RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		String token = request.getHeader(ConstsPool.TOKEN_KEY);
		if (StringUtils.isBlank(token)) {
			// 返回请求未登录
			ApiResponseUtil.renderJson(response, ApiStatus.UNAUTHORIZED, null);
			return false;
		}
		Claims claims;
		// 校验token是否过期
		try {
			// 解析token，如果过期了会抛出异常
			// 如果和Redis中存储的token不一致，说明用户在其他地方登录了，会抛出未登录异常
			claims = JwtUtil.parse(token);
		} catch (Exception e) {
			ApiResponseUtil.renderJson(response, ApiStatus.UNAUTHORIZED, null);
			return false;
		}
		if (Boolean.TRUE.equals(claims.get("rememberMe", Boolean.class))) {
			// 如果是记住我，更新过期时间，更新session中的token
			/*String jwt = JwtUtil.create(claims.getId(), claims.getSubject(), true);
			request.setAttribute(Consts.TOKEN_KEY, jwt);*/
			// 更新redis中的token
			JwtUtil.renew(request);
		}
		log.debug("token校验通过");
		// 如果不是，直接过
		return true;

	}
}
