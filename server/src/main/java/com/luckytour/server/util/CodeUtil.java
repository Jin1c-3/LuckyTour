package com.luckytour.server.util;

import com.luckytour.server.config.CodeConfig;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qing
 * @date Created in 2024/2/13 17:35
 */
@Component
public class CodeUtil {

	private final CodeConfig codeConfig;

	@Autowired
	private CodeUtil(CodeConfig codeConfig) {
		this.codeConfig = codeConfig;
	}

	@PostConstruct
	private void init() {
		UID_MAX_LENGTH = codeConfig.getUserId().getMaxLength();
		UID_MIN_LENGTH = codeConfig.getUserId().getMinLength();
		VERIFICATION_LENGTH = codeConfig.getVerification().getCodeLength();
	}

	private static int UID_MAX_LENGTH;

	private static int UID_MIN_LENGTH;

	private static int VERIFICATION_LENGTH;

	public static String generateVerificationCode() {
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < VERIFICATION_LENGTH; i++) {
			code.append(RandomUtils.nextInt(0, 10));
		}
		return code.toString();
	}

	public static String generateUserId() {
		StringBuilder id = new StringBuilder();
		int length = RandomUtils.nextInt(UID_MIN_LENGTH, UID_MAX_LENGTH + 1);
		for (int i = 0; i < length; i++) {
			id.append(RandomUtils.nextInt(0, 10));
		}
		return id.toString();
	}
}
