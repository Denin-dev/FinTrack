package br.com.fintrack;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class FintrackApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void deveExecutarMetodoMain() {

		String[] args = {};

		try (MockedStatic<SpringApplication> spring = mockStatic(SpringApplication.class)) {

			FintrackApplication.main(args);

			spring.verify(() -> SpringApplication.run(FintrackApplication.class, args));;
		}
	}

}
