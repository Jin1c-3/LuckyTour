package com.luckytour.server;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import com.luckytour.server.config.ExternalApiConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author yujin
 */
@SpringBootApplication
@MapperScan("com.luckytour.server.mapper")
@EnableConfigurationProperties(ExternalApiConfig.class)
@EnableMPP
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
