package br.com.jzbreno.model.DTO;

import br.com.jzbreno.serializer.GenderSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

//podemos usar o @JsonPropertyOrder para definir a ordem dos atributos da classe no json
//@JsonPropertyOrder ({ "id", "first_name", "last_Name", "gender", "address"})
@Data
public class PersonDTO implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
//    @JsonProperty("first_name")
    private String firstName;
//    @JsonProperty("last_name")
    private String lastName;
@JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    private String address;

//    @JsonIgnore
    @JsonSerialize(using = GenderSerializer.class)
    private String gender;



}

