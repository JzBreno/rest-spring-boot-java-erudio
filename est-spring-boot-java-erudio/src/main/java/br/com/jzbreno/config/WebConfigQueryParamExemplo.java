package br.com.jzbreno.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
//@Configuration
//classe de exemplo
public class WebConfigQueryParamExemplo {


    // via extension . http://localhost:8080/pc/findAll.xml or http://localhost:8080/pc/findAll.json was deprecated on spring boot 2.6
    // via query param http://localhost:8080/pc/findAll?mediaType=xml
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true)
                .parameterName("MediaType") /// nome da variavel na query
                .ignoreAcceptHeader(true) /// ignora o header da requisicao, ja que abaixo iremos setar
                .useRegisteredExtensionsOnly(false) /// setaremos como false pois iremos customizar nossas externsoes
                .defaultContentType(MediaType.APPLICATION_JSON) /// tipo padrao
                .mediaType("xml", MediaType.APPLICATION_XML); /// novo tipo suportado

    }

}
