package com.netshoes.wishlist.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Wishlist API - Netshoes Case",
                version = "1.0",
                description = "API para gerenciamento de wishlist de clientes no e-commerce.",
                contact = @Contact(
                        name = "Leonardo Rodrigues Dantas",
                        email = "leonardordnt1317@gmail.com",
                        url = "https://github.com/leonardodantas"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Ambiente Local")
        }
)
public class OpenApiConfig {
}
