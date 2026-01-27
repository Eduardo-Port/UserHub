package io.github.Eduardo_Port.userhubapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Hub API")
                        .description("API para estudos com uma CRUD básica com foco em explorar e conhecer mais sobre dockerização, testes unitários, JPA e padrões de projetos.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Eduardo Oliveira")
                                .email("eduardo17.pro17@gmail.com")
                                .url("https://www.linkedin.com/in/eduardo-oliveira239")));
    }
}
