package com.luckytour.server;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * Jasypt 加密测试
 * cmd 启动方式：java -jar -Djasypt.encryptor.password=${JASYPT_PASSWORD} xxx.jar
 * </p>
 *
 * @author qing
 * @date Created in 2023/8/2 9:43
 */
@SpringBootTest(classes = TestJasypt.class)
class TestJasypt {
	@Test
	void generateEncrypt() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("0x31cYJY@sb");//标准0x...sb密码
		// 默认值
		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		System.out.println(encryptor.encrypt("root"));//需要修改
	}
}
