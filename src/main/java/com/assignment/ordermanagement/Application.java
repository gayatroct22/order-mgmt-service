package com.assignment.ordermanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Application.java
@SpringBootApplication
@ComponentScan(basePackages = "com.assignment.ordermanagement")
@EnableSwagger2
@Slf4j
public class Application {
	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		log.info("Order Management Service started ...");
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.assignment.ordermanagement"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("My API Documentation")
				.description("API documentation for my Spring Boot application")
				.version("1.0")
				.build();
	}
}
