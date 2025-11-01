package br.com.jzbreno.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    //https://www.baeldung.com/spring-boot-openapi-3
    //o bean e uma classe gerenciada pelo spring, onde ele toma de conta da forma que ele deve ser instanciado e do relacionamento dele com outros beans
    //injecao de dependencia
    @Bean
    OpenAPI customOpenApiConfig(){
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("RESTful API with Spring Boot and Java")
                        .description("RESTful API with Spring Boot and Java")
                        .version("v1")
                        .termsOfService("https://github.com/jzbreno/est-spring-boot-java-erudio")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/jzbreno/est-spring-boot-java-erudio")));
    }
}
