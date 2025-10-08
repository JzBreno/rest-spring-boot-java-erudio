package br.com.jzbreno.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
//podemos usar o @JsonPropertyOrder para definir a ordem dos atributos da classe no json
@JsonPropertyOrder ({ "id", "first_name", "last_Name", "gender", "address"})
@Data
public class PersonDTO implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String address;

    @JsonIgnore
    private String gender;



}

