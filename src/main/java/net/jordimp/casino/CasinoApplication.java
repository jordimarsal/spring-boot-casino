package net.jordimp.casino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = "net.jordimp.casino")
@EnableJpaRepositories
public class CasinoApplication {

  public static void main(String[] args) {
    SpringApplication.run(CasinoApplication.class, args);
  }
}
