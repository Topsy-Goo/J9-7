package ru.gb.antonov.J97;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.J97")
public class J97Application {

	public static void main(String[] args) {
		SpringApplication.run (J97Application.class, args);
	}

    public static boolean isStringValid (String s) {    return s != null && !s.isBlank();    }
}
