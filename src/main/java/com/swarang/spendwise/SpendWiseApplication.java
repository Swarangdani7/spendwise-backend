package com.swarang.spendwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SpendWiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpendWiseApplication.class, args);
	}

}
