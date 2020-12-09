package com.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author wlfei
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class })
public class Application {
	public static void main(String[] args) {
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(Application.class, args);
		System.out.println("--------------------");
		System.out.println("-- start success! --");
		System.out.println("--------------------");
	}
}
