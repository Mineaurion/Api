package com.mineaurion.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Bean
	Faker faker(){
		return new Faker();
	}

}

