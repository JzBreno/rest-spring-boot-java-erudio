package br.com.jzbreno.model.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;



}

