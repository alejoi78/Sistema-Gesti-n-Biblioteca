package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.UserRegistrationDetails;
import com.example.demo.mail.EmailDetails;
import com.example.demo.mail.EmailService;
import com.example.demo.models.Usuarios;
import com.example.demo.repositories.UsuariosRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/recuperar")
public class ContraController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> recoverAccount(@RequestBody UserRegistrationDetails request) {
        String correoelectronico = request.getcorreoelectronico();

        // Buscar el usuario por email
        Usuarios usuario = usuariosRepository.findByCorreoelectronico(correoelectronico).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró ningún usuario con el correo especificado.");
        }

        // Verificar si el nombre no es null
        if (usuario.getNombre() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("El nombre del usuario no está disponible.");
        }

        try {
            // Generar una nueva contraseña temporal
            String nuevaContraseña = generateTemporaryPassword();
            System.out.println("Contraseña temporal generada: " + nuevaContraseña); // Verifica la contraseña

            // Encriptar la nueva contraseña antes de guardarla
            usuario.setPassword(passwordEncoder.encode(nuevaContraseña));

            // Guardar el usuario con la nueva contraseña
            usuariosRepository.save(usuario);

            // Crear los detalles para el correo
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(usuario.getCorreoelectronico());
            emailDetails.setNombre(usuario.getNombre());
            emailDetails.setTemporaryPassword(nuevaContraseña);

            // Enviar el correo de recuperación de contraseña
            emailService.sendPassword(emailDetails);

            System.out.println("Correo de recuperación enviado a: " + correoelectronico);
            return ResponseEntity.ok("Correo de recuperación enviado.");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo de recuperación.");
        }
    }

    private String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[12]; // Genera 12 bytes aleatorios
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes); // Codifica los bytes en Base64
    }
}
