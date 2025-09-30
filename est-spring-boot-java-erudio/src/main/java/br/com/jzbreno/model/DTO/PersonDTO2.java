package br.com.jzbreno.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PersonDTO2 implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;


}

