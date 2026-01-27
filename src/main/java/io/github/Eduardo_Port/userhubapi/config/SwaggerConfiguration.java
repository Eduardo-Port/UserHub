package io.github.Eduardo_Port.userhubapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@OpenAPIDefinition
@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openAPI(Environment environment) {
        String contactName = environment.getProperty("contact.name");
        String contactEmail = environment.getProperty("contact.email");
        String contactUrl = environment.getProperty("contact.url");
        Contact contact = new Contact()
                .name(contactName)
                .email(contactEmail)
                .url(contactUrl);
        License license = new License()
                .name(environment.getProperty("license"))
                .url(environment.getProperty("url"));
        return new OpenAPI()
                .info(
                        new Info()
                                .title(environment.getProperty("title"))
                                .description("description")
                                .contact(contact)
                                .version("version")
                                .license(license)
                );

    }
}
