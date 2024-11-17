package com.example.demo.models; // Asegúrate de que el paquete sea el correcto

public class CompraRequest {
    private String tituloLibro;
    private String username;

    // Constructor vacío
    public CompraRequest() {
    }

    // Getters y setters
    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
