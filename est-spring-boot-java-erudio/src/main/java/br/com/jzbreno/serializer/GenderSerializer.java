package br.com.jzbreno.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
//criando um serializer para o atributo gender
//trocara o nome pela primeira letra do atributo
public class GenderSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String gender = s.equalsIgnoreCase("Macho") ? "M" : "F";
        jsonGenerator.writeString(gender);
    }
}
