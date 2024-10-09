package com.example.demo;

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

    // Method 3: To send an email with HTML content
    @Override
public String sendHtmlMail(EmailDetails details) {
    try {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(sender);
        helper.setTo(details.getRecipient());
        helper.setSubject(details.getSubject());

        // Contenido HTML
        String htmlContent = """
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
                    <h1>¡Bienvenido a Nuestra Empresa!</h1>
                    <p>Gracias por unirte a nosotros. Estamos emocionados de tenerte a bordo.</p>
                    <p>Si tienes alguna duda, no dudes en contactarnos.</p>
                    <div class="footer">
                        <p>© 2024 Nuestra Empresa. Todos los derechos reservados.</p>
                    </div>
                </div>
            </body>
            </html>
            """;

        helper.setText(htmlContent, true); // 'true' indica que el contenido es HTML

        javaMailSender.send(mimeMessage);
        return "Correo HTML enviado exitosamente";
    } catch (MessagingException e) {
        return "Error al enviar el correo HTML";
    }
}

}

