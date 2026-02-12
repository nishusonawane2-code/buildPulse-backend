package com.nisha.construction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.nisha.construction.auth.service.AuthService;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ConstructionBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();

		if (dotenv.entries().isEmpty()) {
			dotenv = Dotenv.configure()
					.directory("../")
					.ignoreIfMissing()
					.load();
		}

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ConstructionBackendApplication.class, args);
		System.out.println("Server is running.....");
	}

	@Bean
	CommandLineRunner init(AuthService authService) {
		return args -> {
			authService.registerAdmin();
		};
	}

}
