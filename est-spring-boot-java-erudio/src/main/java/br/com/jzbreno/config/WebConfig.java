package br.com.jzbreno.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    // via extension . http://localhost:8080/pc/findAll.xml or http://localhost:8080/pc/findAll.json was deprecated on spring boot 2.6
    // via query param http://localhost:8080/pc/findAll?mediaType=xml
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        no headers iremos incerir a key accept com o valor xml ou jsona(application/json ou application/xml)
//        melhor modo para evitar muitos parametros na url
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false) /// setaremos como false pois iremos customizar nossas externsoes
                .defaultContentType(MediaType.APPLICATION_JSON) /// tipo padrao
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("yaml", MediaType.APPLICATION_YAML); /// novo tipo suportado
    }

}
