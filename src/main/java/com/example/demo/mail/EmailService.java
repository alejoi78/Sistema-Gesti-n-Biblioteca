package com.example.demo.mail;

import java.util.Date;

public interface EmailService {

    // Métodos del servicio de correo
    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);

    String sendHtmlMail(EmailDetails details);

    String sendPassword(EmailDetails details);

    String enviarCorreoCompra(String nombreusuario, String destinatario, String tituloLibro, String autorLibro,
            double precio, Date fechaCompra);
}
