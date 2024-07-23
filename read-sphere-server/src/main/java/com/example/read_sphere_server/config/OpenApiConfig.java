package com.example.read_sphere_server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition (
    info = @Info(
            contact = @Contact(
                    name = "Sharad Aghara",
                    email = "sharad@example.com",
                    url = "https://www.ai.com"
            ),
            description = "OpenApi documentation for Spring Security",
            title = "OpenApi specification - Sharad",
            version = "1.0",
            license = @License(
                    name = "Licence Name",
                    url = "licence.url"
            ),
            termsOfService = "Terms of Service"
    ),
    servers = {
    @Server(
            description = "Local ENV",
            url = "http://localhost:8088/api/v1"
    ),
    @Server(
            description = "Production ENV",
            url = "purchesed.deployed.domail.com"
    )
    },
    security = {
            @SecurityRequirement(
                    name = "bearerAuth"
            )
    }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {
}
