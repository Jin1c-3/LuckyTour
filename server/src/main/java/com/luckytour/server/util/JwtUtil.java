package com.luckytour.server.util;

import com.luckytour.server.common.constant.ApiStatus;
import com.luckytour.server.common.constant.Consts;
import com.luckytour.server.exception.SecurityException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author qing
 * @date Created in 2023/8/2 17:02
 */
@Slf4j
@Component
public class JwtUtil {

	@Value("${jwt.config.secret-key}")
	private String secretKey;

	@Value("${jwt.config.short-ttl}")
	private Long shortTtl;

	@Value("${jwt.config.long-ttl}")
	private Long longTtl;

	@PostConstruct
	public void init() {
		SECRET_KEY = secretKey;
		TTL_SHORT = shortTtl;
		TTL_LONG = longTtl;
	}

	/**
	 * jwt 加密 key，默认值：yutech.
	 */
	private static String SECRET_KEY;

	/**
	 * jwt 过期时间，默认值：600000 {@code 10 分钟}.
	 */
	private static Long TTL_SHORT;

	/**
	 * 开启 记住我 之后 jwt 过期时间，默认值 604800000 {@code 7 天}
	 */
	private static Long TTL_LONG;

	/**
	 * 创建JWT并存入Redis
	 *
	 * @param rememberMe 记住我
	 * @param id         用户id
	 * @param subject    用户名
	 * @return JWT
	 */
	public static String create(String id, String subject, Boolean rememberMe) {
		Date now = new Date();
		JwtBuilder builder = Jwts.builder()
				.setId(id)
				.setSubject(subject)
				.addClaims(Map.of("rememberMe", rememberMe))
				.setIssuedAt(now)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY);

		// 设置过期时间
		Long ttl = Boolean.TRUE.equals(rememberMe) ? TTL_LONG : TTL_SHORT;
		// 不设置token自带的过期时间，由redis来控制
		/*if (ttl > 0) {
			builder.setExpiration(DateUtil.offset(now, ttl.intValue(), ChronoUnit.MILLIS));
		}*/

		String jwt = builder.compact();
		String key = Consts.REDIS_JWT_KEY_PREFIX + id;
		// 将生成的JWT保存至Redis，先删除之前的
		if (RedisUtil.hasKey(key)) {
			RedisUtil.del(key);
		}
		RedisUtil.set(key, jwt);
		log.debug("存储的redis是：{}", RedisUtil.get(key));
		RedisUtil.expire(key, ttl, TimeUnit.MILLISECONDS);
		return jwt;
	}

	/**
	 * 解析JWT
	 *
	 * @param jwt JWT
	 * @return {@link Claims}
	 */
	public static Claims parse(String jwt) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(SECRET_KEY)
					.parseClaimsJws(jwt)
					.getBody();

			String id = claims.getId();
			String redisKey = Consts.REDIS_JWT_KEY_PREFIX + id;

			// 校验redis中的JWT是否存在
			if (!RedisUtil.hasKey(redisKey)) {
				throw new SecurityException(ApiStatus.TOKEN_EXPIRED);
			}

			// 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
			String redisToken = RedisUtil.get(redisKey);
			if (!jwt.equals(redisToken)) {
				log.debug("jwt:{};||redis:{}", jwt, redisToken);
				throw new SecurityException(ApiStatus.TOKEN_OUT_OF_CTRL);
			}
			return claims;
		} catch (ExpiredJwtException e) {
			log.error("Token 已过期");
			throw new SecurityException(ApiStatus.TOKEN_EXPIRED);
		} catch (UnsupportedJwtException e) {
			log.error("不支持的 Token");
			throw new SecurityException(ApiStatus.TOKEN_PARSE_ERROR);
		} catch (MalformedJwtException e) {
			log.error("Token 无效");
			throw new SecurityException(ApiStatus.TOKEN_PARSE_ERROR);
		} catch (SignatureException e) {
			log.error("无效的 Token 签名");
			throw new SecurityException(ApiStatus.TOKEN_PARSE_ERROR);
		} catch (IllegalArgumentException e) {
			log.error("Token 参数不存在");
			throw new SecurityException(ApiStatus.TOKEN_PARSE_ERROR);
		}
	}

	/**
	 * 设置JWT过期
	 *
	 * @param request 请求
	 */
	public static void invalidate(HttpServletRequest request) {
		String jwt = getJwtFromRequest(request);
		String id = getIdFromJwt(jwt);
		// 从redis中清除JWT
		RedisUtil.del(Consts.REDIS_JWT_KEY_PREFIX + id);
	}

	/**
	 * 在redis中续期JWT
	 *
	 * @param request 请求
	 */
	public static void renew(HttpServletRequest request) {
		String jwt = getJwtFromRequest(request);
		String id = getIdFromJwt(jwt);
		// 从redis中清除JWT
		RedisUtil.expire(Consts.REDIS_JWT_KEY_PREFIX + id, TTL_LONG, TimeUnit.MILLISECONDS);
	}

	/**
	 * 根据 jwt 获取用户名
	 *
	 * @param jwt JWT
	 * @return 用户名
	 */
	private static String getIdFromJwt(String jwt) {
		return parse(jwt).getId();
	}

	/**
	 * 从 request 的 session 中获取 JWT
	 *
	 * @param request 请求
	 * @return JWT
	 */
	private static String getJwtFromRequest(HttpServletRequest request) {
		String jwt = request.getHeader(Consts.TOKEN_KEY);
		if (StringUtils.isNoneBlank(jwt)) {
			return jwt;
		}
		log.warn("JWT 为空");
		return null;
//		String bearerToken = request.getHeader("Authorization");
//		if (StringUtils.isNoneBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
//			return bearerToken.substring(7);
//		}
//		return null;
	}
}
