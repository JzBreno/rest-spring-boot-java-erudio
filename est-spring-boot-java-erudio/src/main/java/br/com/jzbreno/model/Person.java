package br.com.jzbreno.model;

import jakarta.persistence.*;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "person")
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
    @Column(name = "address", nullable = false, length = 80)
    private String address;
    @Column(name = "wikipedia_profile_url", length = 255)
    private String wikipediaUrl;
    @Column(name = "photo_url", length = 255)
    private String photoUrl;
    @Column(name = "gender", nullable = false, length = 6)
    private String gender;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(nullable = false)
    private Boolean enabled = true;
    //mapeando tabelas
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Book> books;
}
