package com.example.itmo.extended;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class ItmoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItmoApplication.class, args);
	}

}
