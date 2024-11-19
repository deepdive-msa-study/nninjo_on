package com.nninjoon.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI()
			.addSecurityItem(
				new SecurityRequirement().addList("Bearer Authentication")
			)
			.components(
				new Components().addSecuritySchemes
					("Bearer Authentication", createAPIKeyScheme())
			)
			.addServersItem(new Server().url("/"))
			.info(
				new Info().title("MSA Blog")
					.description("MSA Blog API Spec")
					.version("v1.0.0")
			);
	}

	public SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
			.bearerFormat("JWT")
			.scheme("bearer");
	}
}
