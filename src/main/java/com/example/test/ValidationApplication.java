package com.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
    info = @Info(title = "My API", version = "1.0"),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Dev")
    }
)
@EnableAsync
@SpringBootApplication
public class ValidationApplication {

  public static void main(String[] args) {
    SpringApplication.run(ValidationApplication.class, args);
  }
}
