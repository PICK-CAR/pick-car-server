package dev.bang.pickcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PickCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickCarApplication.class, args);
	}
}
