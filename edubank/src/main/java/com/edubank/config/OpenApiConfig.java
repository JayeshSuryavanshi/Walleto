package com.edubank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	private static final String SERVICE_AUTH = "ServiceAuth";

	@Bean
	public OpenAPI bankApiOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Walleto bank-api")
						.description("Lean, secure REST core-banking service consumed server-to-server by wallet-api")
						.version("1.0.0"))
				.addSecurityItem(new SecurityRequirement().addList(SERVICE_AUTH))
				.components(new Components().addSecuritySchemes(SERVICE_AUTH,
						new SecurityScheme()
								.type(SecurityScheme.Type.APIKEY)
								.in(SecurityScheme.In.HEADER)
								.name("X-Service-Auth")));
	}
}
