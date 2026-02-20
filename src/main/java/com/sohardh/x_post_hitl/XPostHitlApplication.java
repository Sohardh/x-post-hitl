package com.sohardh.x_post_hitl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XPostHitlApplication {

	public static void main(String[] args) {
		SpringApplication.run(XPostHitlApplication.class, args);
	}

}
