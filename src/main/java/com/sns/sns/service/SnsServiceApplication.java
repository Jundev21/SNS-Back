package com.sns.sns.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SnsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnsServiceApplication.class, args);
	}

}
