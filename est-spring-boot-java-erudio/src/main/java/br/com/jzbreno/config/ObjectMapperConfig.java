package br.com.jzbreno.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        //esse mapper faz a serializacao em json, direto do jackson
        ObjectMapper objectMapper = new ObjectMapper();
//        ao anotar como @bean o objectMapper nao acha a config de ajuste do jackson para localdate, tendo que setar manualmente desta forma
        objectMapper.registerModule(new JavaTimeModule());
//        definindo filtro sobre json que iremos enviar, iremos incluir o nome dos atributos dentro do filter
        SimpleFilterProvider filters = new SimpleFilterProvider()
                .addFilter("PersonFilter", SimpleBeanPropertyFilter.serializeAllExcept("email"));
//        temos que adicionar o filtro ao nosso mapper
        objectMapper.setFilters(filters);
        return objectMapper;
    }

}
