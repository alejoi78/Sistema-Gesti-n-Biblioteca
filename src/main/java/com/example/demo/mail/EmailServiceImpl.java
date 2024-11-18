package com.example.demo.mail;

import java.io.File;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Date;

// Annotation
@Service
// Class implementing EmailService interface
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    // Method 1: To send a simple email
    @Override
    public String sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            return "Correo enviado exitosamente";
        } catch (Exception e) {
            return "Error al enviar el correo";
        }
    }

    // Method 2: To send an email with attachment
    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            if (details.getAttachment() != null && !details.getAttachment().isEmpty()) {
                FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
                mimeMessageHelper.addAttachment(file.getFilename(), file);
            }

            javaMailSender.send(mimeMessage);
            return "Correo con adjunto enviado exitosamente";
        } catch (MessagingException e) {
            return "Error al enviar el correo con adjunto";
        }
    }

    @Override
    public String sendHtmlMail(EmailDetails details) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            String nombreUsuario = details.getNombre();
            if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                nombreUsuario = "Usuario"; // Si el nombre está vacío o null, asigna un valor por defecto
            }

            // Contenido HTML usuario nuevo
            String htmlContent = String.format(
                    """
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <style>
                                    body {
                                        font-family: Arial, sans-serif;
                                        background-color: #f4f4f4;
                                        padding: 20px;
                                    }
                                    .email-container {
                                        background-color: white;
                                        padding: 20px;
                                        border-radius: 10px;
                                        max-width: 600px;
                                        margin: auto;
                                        box-shadow: 0 0 10px rgba(0,0,0,0.1);
                                    }
                                    h1 {
                                        color: #333;
                                    }
                                    p {
                                        color: #666;
                                    }
                                    .footer {
                                        margin-top: 20px;
                                        text-align: center;
                                        font-size: 12px;
                                        color: #999;
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="email-container">
                                    <h1>¡Bienvenido a BookHaven!</h1>
                                    <p>Hola %s,</p>
                                    <p>Gracias por registrarte en BookHaven. Estamos emocionados de que te unas a nuestra comunidad.</p>
                                    <p>Tu cuenta ha sido creada exitosamente.</p>
                                    <div class="footer">
                                        <p>© 2024 BookHaven. Todos los derechos reservados.</p>
                                    </div>
                                </div>
                            </body>
                            </html>
                            """,
                    nombreUsuario, details.getNombre());

            helper.setText(htmlContent, true); // 'true' indica que el contenido es HTML

            javaMailSender.send(mimeMessage);
            return "Correo HTML enviado exitosamente";
        } catch (MessagingException e) {
            return "Error al enviar el correo HTML";
        }
    }

    // Método específico para el correo de recuperación de contraseña
    @Override
    public String sendPassword(EmailDetails emailDetails) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject("Recuperación de cuenta - BookHaven");

            // Verifica si el nombre está disponible
            String nombreUsuario = emailDetails.getNombre();
            if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                nombreUsuario = "Usuario"; // Si el nombre está vacío o null, asigna un valor por defecto
            }

            // Cuerpo del correo HTML para recuperación de contraseña
            String htmlContent = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f4f4;
                                padding: 20px;
                            }
                            .email-container {
                                background-color: white;
                                padding: 20px;
                                border-radius: 10px;
                                max-width: 600px;
                                margin: auto;
                                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                            }
                            h1 {
                                color: #333;
                            }
                            p {
                                color: #666;
                            }
                            .footer {
                                margin-top: 20px;
                                text-align: center;
                                font-size: 12px;
                                color: #999;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <h1>Recuperación de cuenta - BookHaven</h1>
                            <p>Hola %s,</p>
                            <p>Recibimos una solicitud para recuperar tu contraseña.</p>
                            <p>Tu nueva contraseña temporal es: <strong>%s</strong></p>
                            <p>Te recomendamos cambiar tu contraseña después de iniciar sesión.</p>
                            <div class="footer">
                                <p>© 2024 BookHaven. Todos los derechos reservados.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """, nombreUsuario, emailDetails.getTemporaryPassword()); // Aquí se pasa el nombre y la nueva
                                                                              // contraseña

            helper.setText(htmlContent, true); // Usar contenido HTML

            javaMailSender.send(mimeMessage);
            return "Correo de recuperación enviado exitosamente";
        } catch (MessagingException e) {
            return "Error al enviar el correo de recuperación";
        }
    }

    @Override
    public String enviarCorreoCompra(String nombreusuario, String destinatario, String tituloLibro, String autorLibro, String pdfLibro,
            double precio, Date fechaCompra) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(destinatario);
            helper.setSubject("Compra Exitosa - BookHaven");

            String htmlContent = String.format(
                    """
                                               <!DOCTYPE html>
                            <html>
                            <head>
                                <style>
                                    body {
                                        font-family: 'Arial', sans-serif;
                                        background-color: #f9f9f9;
                                        margin: 0;
                                        padding: 0;
                                    }
                                    .email-container {
                                        background-color: #ffffff;
                                        padding: 20px;
                                        margin: 20px auto;
                                        max-width: 600px;
                                        border-radius: 10px;
                                        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
                                    }
                                    .header {
                                        text-align: center;
                                        background-color: #0078d7;
                                        color: #ffffff;
                                        padding: 15px;
                                        border-radius: 10px 10px 0 0;
                                    }
                                    .header h1 {
                                        margin: 0;
                                        font-size: 24px;
                                    }
                                    .content {
                                        padding: 20px;
                                        color: #333333;
                                    }
                                    .content h2 {
                                        color: #0078d7;
                                        margin-bottom: 10px;
                                    }
                                    .content p {
                                        line-height: 1.6;
                                        color: #555555;
                                    }
                                    .content ul {
                                        list-style: none;
                                        padding: 0;
                                    }
                                    .content ul li {
                                        background-color: #f4f4f4;
                                        margin: 10px 0;
                                        padding: 10px;
                                        border-radius: 5px;
                                        font-size: 16px;
                                    }
                                    .footer {
                                        text-align: center;
                                        padding: 10px;
                                        font-size: 12px;
                                        color: #999999;
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="email-container">
                                    <div class="header">
                                        <h1>¡Gracias por tu compra!</h1>
                                        <p>Hola %s,</p>
                                    </div>
                                    <div class="content">
                                        <h2>Detalles de tu pedido:</h2>
                                        <ul>
                                            <li><strong>Título:</strong> %s</li>
                                            <li><strong>Autor:</strong> %s</li>
                                            <li><strong>Precio:</strong> $%.2f</li>
                                            <li><strong>Fecha:</strong> %s</li>
                                            <li><strong>Link PDF:</strong> %s</li>
                                        </ul>
                                        <p>Estamos encantados de que hayas elegido **BookHaven** para tus necesidades literarias. Esperamos que disfrutes tu nuevo libro.</p>
                                        <p>Si tienes alguna pregunta o necesitas ayuda, no dudes en ponerte en contacto con nuestro equipo de soporte.</p>
                                    </div>
                                    <div class="footer">
                                        <p>© 2024 BookHaven. Todos los derechos reservados.</p>
                                        <p>Sigue explorando nuevos mundos en cada página.</p>
                                    </div>
                                </div>
                            </body>
                            </html>

                                    """,
                    nombreusuario, tituloLibro, autorLibro, precio, fechaCompra.toString(), pdfLibro);

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            return "Correo de compra enviado exitosamente";
        } catch (MessagingException e) {
            return "Error al enviar el correo de compra";
        }
    }

}
