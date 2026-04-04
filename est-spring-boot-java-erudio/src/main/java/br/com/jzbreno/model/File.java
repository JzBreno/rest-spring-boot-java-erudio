package br.com.jzbreno.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
@Entity
@Table(name = "files")
@Data
public class File implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="path",nullable = false)
    private String path;
    @Column(name="filesize",nullable = false)
    private Long fileSize;
    @Column(name="filename",nullable = false)
    private String fileName;

}
