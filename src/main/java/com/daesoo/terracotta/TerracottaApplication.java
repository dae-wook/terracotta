package com.daesoo.terracotta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8080", description = "Dev Server URL"), @Server(url = "https://terracotta.site", description = "Default Server URL")})
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class TerracottaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerracottaApplication.class, args);
	}

}
