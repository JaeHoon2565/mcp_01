package com.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class McpServerApplication {

	public static void main(String[] args) {


		SpringApplication.run(McpServerApplication.class, args);


	}

}
