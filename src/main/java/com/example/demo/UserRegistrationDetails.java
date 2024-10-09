package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDetails {
    private String nombre;
    private String apellido;
    private String correoelectronico;
    private String password;
    private String Username;

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
}
