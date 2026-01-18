package io.github.Eduardo_Port.userhubapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserHubApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserHubApiApplication.class, args);
	}

}
