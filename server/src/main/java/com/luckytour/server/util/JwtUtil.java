package com.luckytour.server.util;

import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.common.http.ServerStatus;
import com.luckytour.server.config.JwtConfig;
import com.luckytour.server.exception.SecurityException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
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

	private final JwtConfig jwtConfig;

	@Autowired
	private JwtUtil(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@PostConstruct
	public void init() {
		ttlShort = jwtConfig.getShortTtl();
		ttlLong = jwtConfig.getLongTtl();
		secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * jwt 加密 key
	 */
	private static Key secretKey;

	/**
	 * jwt 过期时间
	 */
	private static Long ttlShort;

	/**
	 * 开启 记住我 之后 jwt 过期时间
	 */
	private static Long ttlLong;

	/**
	 * 创建JWT并存入Redis
	 *
	 * @param rememberMe 记住我
	 * @param id         用户id
	 * @param subject    用户名
	 * @return JWT
	 */
	public static String createToken(String id, String subject, Boolean rememberMe) {
		Date now = new Date();
		JwtBuilder builder = Jwts.builder()
				.setId(id)
				.setSubject(subject)
				.addClaims(Map.of("rememberMe", rememberMe))
				.setIssuedAt(now)
				.signWith(secretKey);

		// 设置过期时间
		Long ttl = Boolean.TRUE.equals(rememberMe) ? ttlLong : ttlShort;
		// 不设置token自带的过期时间，由redis来控制
		/*if (ttl > 0) {
			builder.setExpiration(DateUtil.offset(now, ttl.intValue(), ChronoUnit.MILLIS));
		}*/

		String jwt = builder.compact();
		String key = ConstsPool.REDIS_JWT_KEY_PREFIX + id;
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
	 * 加密普通字符串
	 *
	 * @param raw 原始字符串
	 * @return 加密后字符串
	 */
	public static String encrypt(String raw) {
		return Jwts.builder()
				.setSubject(raw)
				.signWith(secretKey)
				.compact();
	}

	/**
	 * 验证密码
	 *
	 * @param encrypted 加密后字符串
	 * @return 原始字符串
	 */
	public static boolean matches(String raw, String encrypted) {
		return encrypt(raw).equals(encrypted);
	}

	/**
	 * 解析JWT
	 *
	 * @param jwt JWT
	 * @return {@link Claims}
	 */
	public static Claims parse(String jwt) {
		try {
			JwtParser parser = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build();

			Claims claims = parser
					.parseClaimsJws(jwt)
					.getBody();

			String id = claims.getId();
			String redisKey = ConstsPool.REDIS_JWT_KEY_PREFIX + id;

			// 校验redis中的JWT是否存在
			if (!RedisUtil.hasKey(redisKey)) {
				throw new SecurityException(ServerStatus.TOKEN_EXPIRED);
			}

			// 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
			String redisToken = RedisUtil.get(redisKey);
			if (!jwt.equals(redisToken)) {
				log.debug("jwt:{};||redis:{}", jwt, redisToken);
				throw new SecurityException(ServerStatus.TOKEN_OUT_OF_CTRL);
			}
			return claims;
		} catch (ExpiredJwtException e) {
			log.error("Token 已过期");
			throw new SecurityException(ServerStatus.TOKEN_EXPIRED);
		} catch (UnsupportedJwtException e) {
			log.error("不支持的 Token");
			throw new SecurityException(ServerStatus.TOKEN_PARSE_ERROR);
		} catch (MalformedJwtException e) {
			log.error("Token 无效");
			throw new SecurityException(ServerStatus.TOKEN_PARSE_ERROR);
		} catch (IllegalArgumentException e) {
			log.error("Token 参数不存在");
			throw new SecurityException(ServerStatus.TOKEN_PARSE_ERROR);
		}
	}

	/**
	 * 设置JWT过期
	 *
	 * @param request 请求
	 */
	public static void invalidate(HttpServletRequest request) {
		String jwt = getJwtFromRequest(request);
		String id = parseId(jwt);
		// 从redis中清除JWT
		RedisUtil.del(ConstsPool.REDIS_JWT_KEY_PREFIX + id);
	}

	/**
	 * 在redis中续期JWT
	 *
	 * @param request 请求
	 */
	public static void renew(HttpServletRequest request) {
		String jwt = getJwtFromRequest(request);
		String id = parseId(jwt);
		// 从redis中清除JWT
		RedisUtil.expire(ConstsPool.REDIS_JWT_KEY_PREFIX + id, ttlLong, TimeUnit.MILLISECONDS);
	}

	/**
	 * 根据 jwt 获取 id
	 *
	 * @param jwt JWT
	 * @return 用户名
	 */
	private static String parseId(String jwt) {
		return parse(jwt).getId();
	}

	/**
	 * 根据 jwt 获取 id
	 *
	 * @param request JWT
	 * @return 用户名
	 */
	public static String parseId(HttpServletRequest request) {
		return parseId(request.getHeader(ConstsPool.TOKEN_KEY));
	}

	/**
	 * 从 request 的 session 中获取 JWT
	 *
	 * @param request 请求
	 * @return JWT
	 */
	private static String getJwtFromRequest(HttpServletRequest request) {
		String jwt = request.getHeader(ConstsPool.TOKEN_KEY);
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
