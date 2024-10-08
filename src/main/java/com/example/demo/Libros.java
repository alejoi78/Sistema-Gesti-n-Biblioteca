package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int contador = 0;
    private String titulo;
    private String autor;
    private String isbn;
    private int numeroPaginas;
    private String editorial;
    private int anioPublicacion;
    private double precio;
    private String genero;
    private String idioma;
    private String formato;
}
