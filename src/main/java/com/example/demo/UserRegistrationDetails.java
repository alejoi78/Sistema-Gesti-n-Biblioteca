package com.example.demo;

import com.example.demo.models.Roles;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDetails {
    private String nombre;
    private String apellido;
    private String correoelectronico;
    private String password;
    private String Username;
    private Roles rol;

    public UserRegistrationDetails(String nombre, String apellido, String correoelectronico, String password, String Username, Roles rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoelectronico = correoelectronico;
        this.password = password;
        this.Username = Username;
        this.rol = rol;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getcorreoelectronico() {
        return correoelectronico;
    }

    public void setEmail(String correoelectronico) {
        this.correoelectronico = correoelectronico;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsarname() {
        return this.Username;
    }

    public void setUsarname(String Username) {
        this.Username = Username;
    }

    public Roles getRol() {
        return rol; 
    }

    public void setRol(Roles rol) {
        this.rol = rol; 
    }
}
