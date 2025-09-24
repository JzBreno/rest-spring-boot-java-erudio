package br.com.jzbreno.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "person")
@Slf4j
@Data
public class Person implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    @Column(name = "gender", nullable = false, length = 6)
//    @Enumerated(EnumType.STRING)
    private String gender;

}
