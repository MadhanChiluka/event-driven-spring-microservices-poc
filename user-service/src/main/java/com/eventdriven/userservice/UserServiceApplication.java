package com.eventdriven.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

import com.eventdriven.commonservice.config.AxonConfig;

@SpringBootApplication
@EntityScan("com.eventdriven.**")
@Import({ AxonConfig.class })
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
