package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Usuarios")
public class Usuarios {
    @Id
    @Column(name= "ID")
    private int id;

    private String nombre;


}



