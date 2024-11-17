package com.example.demo.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String recipient; // Cambiado a minúscula para cumplir con las convenciones
    private String msgBody;
    private String subject;
    private String attachment;
    private String nombre; // Campo para el nombre del usuario
    private String temporaryPassword; // Campo para la contraseña temporal
    private String destinatario; // Campo para el destinatario
    private String tituloLibro; // Título del libro comprado
    private String autorLibro; // Autor del libro
    private double precio; // Precio del libro
    private Date fechaCompra; // Fecha de la compra
    private String nombreusuario;
}
