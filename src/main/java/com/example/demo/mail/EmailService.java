package com.example.demo.mail;

public interface EmailService {

    // Método para enviar un correo simple
    String sendSimpleMail(EmailDetails details);

    // Método para enviar un correo con adjunto
    String sendMailWithAttachment(EmailDetails details);

    // Método para enviar un correo en formato HTML
    String sendHtmlMail(EmailDetails details);

    String sendpassword(EmailDetails details);
}
