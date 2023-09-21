package com.jiggycode;

import com.jiggycode.author.Author;
import com.jiggycode.author.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner runner(AuthorRepository authorRepository) {
		return args -> {
			Author stanimal = new Author(
					"Stanimal",
					"stanimal@gmail.com",
					26
			);

			Author jiggy = new Author(
					"Jiggy",
					"jiggy@gmail.com",
					25
			);

			List<Author> authors = List.of(stanimal, jiggy);
//			authorRepository.saveAll(authors);
		};
	}

}
