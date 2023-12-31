package com.jiggycode;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.jiggycode.author.Author;
import com.jiggycode.author.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner runner(
			AuthorRepository authorRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			var faker = new Faker();
			Random random = new Random();
			Name name = faker.name();
			String firstName = name.firstName();
			String lastName = name.lastName();
			Author author = new Author(
					firstName + " " + lastName,
					firstName.toLowerCase() + "." + lastName.toLowerCase() + "@jiggycode.com",
                    passwordEncoder.encode(UUID.randomUUID().toString()), random.nextInt(16, 99)
			);
			authorRepository.save(author);
		};
	}

}
