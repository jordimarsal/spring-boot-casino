package net.jordimp.casino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CasinoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasinoApplication.class, args);
	}

}
