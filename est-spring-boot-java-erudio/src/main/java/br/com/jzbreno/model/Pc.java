package br.com.jzbreno.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "video_card", nullable = false, length = 40)
    private String videoCard;
    @Column(name = "cpu", nullable = false, length = 40)
    private String processor;
    @Column(name = "RAM_memory", nullable = false, length = 40)
    private String ramMemory;
    @Column(name = "storage_unit", nullable = false, length = 40)
    private String storageUnit;

}
