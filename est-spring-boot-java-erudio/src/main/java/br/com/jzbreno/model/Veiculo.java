package br.com.jzbreno.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "veiculo")
@Data
public class Veiculo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "placa", nullable = false, length = 10)
    private String placa;

    @Column(name = "marca", nullable = false, length = 80)
    private String marca;

    @Column(name = "modelo", nullable = false, length = 80)
    private String modelo;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "cor", nullable = false, length = 40)
    private String cor;
}
