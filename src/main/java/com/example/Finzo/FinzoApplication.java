package com.example.Finzo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinzoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinzoApplication.class, args);
	}

}
