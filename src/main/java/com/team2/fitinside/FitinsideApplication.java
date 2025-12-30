package com.team2.fitinside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class FitinsideApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitinsideApplication.class, args);
	}

}
