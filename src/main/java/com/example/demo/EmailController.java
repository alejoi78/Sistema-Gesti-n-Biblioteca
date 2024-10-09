package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Annotation
@RestController
// Class
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Enviar correo simple
    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody EmailDetails details) {
        String status = emailService.sendSimpleMail(details);
        if ("Correo enviado exitosamente".equals(status)) {
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Enviar correo con adjunto
    @PostMapping("/sendMailWithAttachment")
    public ResponseEntity<String> sendMailWithAttachment(@RequestBody EmailDetails details) {
        String status = emailService.sendMailWithAttachment(details);
        if ("Correo con adjunto enviado exitosamente".equals(status)) {
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Enviar correo HTML
    @PostMapping("/sendHtmlMail")
    public ResponseEntity<String> sendHtmlMail(@RequestBody EmailDetails details) {
        String status = emailService.sendHtmlMail(details);
        if ("Correo HTML enviado exitosamente".equals(status)) {
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
