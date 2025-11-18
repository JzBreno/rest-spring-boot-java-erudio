package br.com.jzbreno.model;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
public class Book implements Serializable {

    private final static Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author", nullable = false, length = 80)
    private String author;
    @Column(name = "title", nullable = false, length = 10)
    private String title;
    @Column(name = "launch_date", nullable = false)
    private LocalDate launch_date;
    @Column(name = "price")
    private double price;

}
