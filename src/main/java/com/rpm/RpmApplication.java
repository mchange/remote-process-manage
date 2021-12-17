package com.rpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.rpm.bean")
@EnableJpaRepositories(basePackages = {"com.rpm.repository"})
public class RpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpmApplication.class, args);
	}

}
