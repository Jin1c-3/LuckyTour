package com.luckytour.server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author qing
 * @date Created in 2023/8/5 9:25
 */
@SpringBootTest(classes = TestDBGenerator.class)
class TestDBGenerator {

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

//	@Value("${spring.datasource.password}")
	private String password="root";

	/**
	 * 需要生成的表
	 */
	private final String[] includedTables = {
			"user"
	};

	@Test
	void generate() {
		System.out.format("url: %s, username: %s, password: %s", url, username, password);
		// 代码生成器
		FastAutoGenerator.create(url, username, password)
				.globalConfig(builder -> {
					builder.author("qing")
							.enableSwagger()
							.enableSpringdoc()
							.outputDir(System.getProperty("user.dir") + "/src/main/java");
				})
				.packageConfig(builder -> {
					builder.parent("com.luckytour.server")
							.entity("entity")
							.mapper("mapper")
							.service("service")
							.serviceImpl("service.serviceImpl")
//							.controller("controller")
							.xml("mapper.xml");
				})
				.strategyConfig(builder -> {
					builder.addInclude(includedTables)
//							.addTablePrefix("t_") // 配置此项可以忽略有该前缀的表
							.entityBuilder()
							.enableLombok()
							.enableTableFieldAnnotation()
//							.logicDeleteColumnName("deleted") // 逻辑删除字段指定
							.enableActiveRecord()
							.naming(NamingStrategy.underline_to_camel) // 数据表与实体类名之间映射的策略
							.columnNaming(NamingStrategy.underline_to_camel) // 数据表的字段与实体类的属性名之间映射的策略
							//不自动生成controller
//							.controllerBuilder()
//							.enableRestStyle() // 开启生成@RestController控制器
//							.formatFileName("%sController")
//							.enableHyphenStyle() // 开启驼峰转连字符
							.serviceBuilder()
							.formatServiceFileName("%sService")
							.superServiceClass(IService.class)
							.formatServiceImplFileName("%sServiceImpl")
							.mapperBuilder()
							.superClass(BaseMapper.class)
							.enableBaseColumnList()
							.enableBaseResultMap()
							.formatMapperFileName("%sMapper")
							.formatXmlFileName("%sMapper");
				})
				.execute();
	}
}
