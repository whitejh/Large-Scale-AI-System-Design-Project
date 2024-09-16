package com.team11.deliveryDriver_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class DeliveryDriverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryDriverServiceApplication.class, args);
	}

}
