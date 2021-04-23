package com.edubank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value={"classpath:messages.properties"})
public class EDUBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(EDUBankApplication.class, args);
	}
}
