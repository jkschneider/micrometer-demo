package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@RestController
class TeamsController {
	// sorted in descending order of importance, of course
	@GetMapping("/teams")
	public List<String> teams() {
		return Arrays.asList("St. Louis Cardinals", "New York Yankees", "Chicago Cubs");
	}
}