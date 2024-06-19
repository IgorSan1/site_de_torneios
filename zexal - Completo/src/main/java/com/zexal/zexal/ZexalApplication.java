package com.zexal.zexal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.zexal.repository")
@SpringBootApplication
public class ZexalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZexalApplication.class, args);
	}

}
