package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    private String nombre;
    private String apellido;
    private String password;
    private String username;
    private String correoelectronico;

    // Relación con Roles
    @ManyToOne
    @JoinColumn(name = "id_rol") // Esta columna se referirá a la tabla de roles
    private Roles rol; // Objeto de la clase Roles
}
