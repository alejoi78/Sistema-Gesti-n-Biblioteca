package com.example.demo.mail;

// Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailDetails {
    // Class data members
    private String Recipient;
    private String MsgBody;
    private String Subject;
    private String Attachment;
    private String Nombre; // Nuevo campo
    private String TemporaryPassword; // Nuevo campo

}
