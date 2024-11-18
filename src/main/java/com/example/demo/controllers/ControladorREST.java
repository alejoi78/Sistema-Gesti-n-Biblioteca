package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.SignatureException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;

import com.example.demo.UserRegistrationDetails;
import com.example.demo.mail.EmailDetails;
import com.example.demo.mail.EmailService;
import com.example.demo.models.Roles;
import com.example.demo.models.Usuarios;
import com.example.demo.repositories.RolRepository;
import com.example.demo.repositories.UsuariosRepository;
import com.example.demo.security.AuthRequest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Controller
public class ControladorREST {

    @Autowired
    private UsuariosRepository usuariosRepository; // Cambia a minúsculas consistentemente

    @Autowired
    private RolRepository rolRepository; // Cambia a minúsculas consistentemente

    @Autowired
    private PasswordEncoder passwordEncoder; // Solo usa la inyección aquí

    @Autowired
    private EmailService emailService;

    // Clave secreta para firmar el JWT
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Página de inicio
    @GetMapping("/")
    public String comienzo() {
        return "vistaIndice";
    }

    // Página del menú
    @GetMapping("/vistamenu")
    public String vistaMenu() {
        return "vistamenu";
    }

    // Página de registrar
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    // Recuperar contraseña
    @GetMapping("/recuperarcontraseña")
    public String recuperarcontraseña() {
        return "recuperarcontraseña";
    }

    // Mi Perfil
    @GetMapping("/perfil")
    public String perfil() {
        return "perfil2.0";
    }

    // Recuperar contraseña
    @GetMapping("/administrar")
    public String administrar() {
        return "administrar";
    }

    // Recuperar contraseña
    @GetMapping("/crudUsuarios")
    public String crudUsuarios() {
        return "crudUsuarios";
    }

    // Método para obtener todos los usuarios (HTTP GET)
    @GetMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<List<Usuarios>> getUsuarios(@RequestHeader("id_rol") int idRol) {
        if (idRol == 1) {
            List<Usuarios> usuarios = usuariosRepository.findAll();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    // Método para agregar un nuevo usuario (HTTP POST)
    // Método para agregar un nuevo usuario (HTTP POST)
    @PostMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<Usuarios> agregarUsuario(@RequestHeader("id_rol") int idRol,
            @RequestBody Usuarios nuevoUsuario) {
        if (idRol == 1) {
            nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword())); // Encriptar la contraseña
            Usuarios usuarioGuardado = usuariosRepository.save(nuevoUsuario);
            return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    // Método para eliminar un usuario por ID (HTTP DELETE)
    @DeleteMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<String> eliminarUsuario(@RequestHeader("id_rol") int idRol, @PathVariable Integer id) {
        if (idRol == 1) {
            if (usuariosRepository.existsById(id)) {
                usuariosRepository.deleteById(id);
                return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
    }

    // Método para actualizar un usuario (HTTP PUT)
    @PutMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<String> editarUsuario(@RequestHeader("id_rol") int idRol, @PathVariable Integer id,
            @RequestBody Usuarios usuarioActualizado) {
        if (idRol == 1) {
            Optional<Usuarios> usuarioExistenteOpt = usuariosRepository.findById(id);
            if (usuarioExistenteOpt.isPresent()) {
                Usuarios usuarioExistente = usuarioExistenteOpt.get();

                // Actualizar todos los campos directamente
                if (usuarioActualizado.getNombre() != null) {
                    usuarioExistente.setNombre(usuarioActualizado.getNombre());
                }
                if (usuarioActualizado.getApellido() != null) {
                    usuarioExistente.setApellido(usuarioActualizado.getApellido());
                }
                if (usuarioActualizado.getUsername() != null) {
                    usuarioExistente.setUsername(usuarioActualizado.getUsername());
                }
                if (usuarioActualizado.getCorreoelectronico() != null) {
                    usuarioExistente.setCorreoelectronico(usuarioActualizado.getCorreoelectronico());
                }
                if (usuarioActualizado.getRol() != null) {
                    usuarioExistente.setRol(usuarioActualizado.getRol());
                }
                // Agrega más campos aquí según sea necesario

                usuariosRepository.save(usuarioExistente);
                return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
    }

    // Nuevo método para la autenticación
    @PostMapping("/auth")
    @ResponseBody
    public ResponseEntity<?> autenticarUsuario(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        // Validar si el usuario y la contraseña son vacíos
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario y la contraseña son requeridos");
        }

        // Buscar el usuario en la base de datos
        Optional<Usuarios> usuarioOpt = usuariosRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuarios usuario = usuarioOpt.get();
            int idUsuario = usuario.getId();
            String nombreUsuario = usuario.getNombre();
            String apellidoUsuario = usuario.getApellido();
            String correoUsuario = usuario.getCorreoelectronico();
            int IdrolUsuario = usuario.getRol().getIdRol();
            String rolUsuario = usuario.getRol().getTipodeRol();

            // Verificar la contraseña usando BCrypt
            if (passwordEncoder.matches(password, usuario.getPassword())) {

                Map<String, Object> claims = new HashMap<>();
                claims.put("rol", rolUsuario); // Añade roles o cualquier otra información
                claims.put("IdRol", IdrolUsuario);
                claims.put("id", idUsuario);
                claims.put("nombre", nombreUsuario);
                claims.put("apellido", apellidoUsuario);
                claims.put("correo", correoUsuario);

                // Fecha de expiración del token (ej. 10 minutos)
                long expirationTime = 1000 * 60 * 120;
                // Generar el token JWT
                String token = Jwts.builder()
                        .setClaims(claims)
                        .setSubject(username)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                        .signWith(SECRET_KEY)
                        .compact();

                // Devolver el token en el cuerpo de la respuesta
                return ResponseEntity.ok(token);

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no registrado");
        }
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
        }

        String token = authHeader.substring(7); // Extrae el token del header

        try {
            // Verifica y extrae los claims del token
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // Si el token es válido, puedes retornar los claims o cualquier información que
            // necesites
            return ResponseEntity.ok(claims);
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el token");
        }
    }

    // Método para registrar un nuevo usuario (HTTP POST)
    @PostMapping("/registrar")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDetails registrationDetails) {
        try {
            // Validar si el nombre de usuario ya existe
            boolean usernameExists = usuariosRepository.existsByUsername(registrationDetails.getUsername());
            // Validar si el correo electrónico ya existe
            boolean emailExists = usuariosRepository
                    .existsByCorreoelectronico(registrationDetails.getcorreoelectronico());

            // Verificar las condiciones y retornar el mensaje adecuado
            if (usernameExists && emailExists) {
                return ResponseEntity.badRequest().body("El nombre de usuario y el correo electrónico ya existen.");
            } else if (usernameExists) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya existe.");
            } else if (emailExists) {
                return ResponseEntity.badRequest().body("El correo electrónico ya está en uso.");
            }

            // Crear el nuevo usuario y configurar sus atributos
            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setNombre(registrationDetails.getNombre());
            nuevoUsuario.setApellido(registrationDetails.getApellido());
            nuevoUsuario.setCorreoelectronico(registrationDetails.getcorreoelectronico());
            nuevoUsuario.setUsername(registrationDetails.getUsername());
            nuevoUsuario.setPassword(passwordEncoder.encode(registrationDetails.getPassword()));

            // Obtener y asignar el rol del usuario
            Roles rol = rolRepository.findById(registrationDetails.getRol().getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            nuevoUsuario.setRol(rol);

            // Guardar el usuario en la base de datos
            Usuarios usuarioGuardado = usuariosRepository.save(nuevoUsuario);

            // Si el usuario se ha guardado exitosamente, enviamos el correo en un nuevo
            // hilo
            if (usuarioGuardado != null) {
                new Thread(() -> {
                    try {
                        // Crear los detalles del correo
                        EmailDetails emailDetails = new EmailDetails();
                        emailDetails.setRecipient(nuevoUsuario.getCorreoelectronico());
                        emailDetails.setSubject("Bienvenido a nuestro servicio");

                        // Cuerpo del correo HTML
                        String htmlBody = """
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
                                        <h1>¡Bienvenido a nuestra plataforma, %s!</h1>
                                        <p>Gracias por registrarte, %s %s.</p>
                                        <p>Estamos emocionados de tenerte con nosotros.</p>
                                        <p>Si tienes alguna duda, no dudes en contactarnos.</p>
                                        <div class="footer">
                                            <p>© 2024 Nuestra Empresa. Todos los derechos reservados.</p>
                                        </div>
                                    </div>
                                </body>
                                </html>
                                """.formatted(nuevoUsuario.getNombre(), nuevoUsuario.getNombre(),
                                nuevoUsuario.getApellido());

                        emailDetails.setMsgBody(htmlBody);
                        emailService.sendHtmlMail(emailDetails); // Enviar correo
                    } catch (Exception e) {
                        // Manejo de errores al enviar el correo
                        System.err.println("Error al enviar el correo: " + e.getMessage());
                    }
                }).start();

                return new ResponseEntity<>("Usuario registrado y bienvenido", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Error al registrar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Ocurrió un error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Insertar automaticamente los roles en la base de datos
    @Configuration
    public class LoadDatabase {

        @Bean
        CommandLineRunner initRolesDatabase(RolRepository rolRepository) {
            return args -> {
                if (rolRepository.findByTipodeRol("Administrador") == null) {
                    rolRepository.save(new Roles(1, "Administrador"));
                }
                if (rolRepository.findByTipodeRol("Usuario") == null) {
                    rolRepository.save(new Roles(2, "Usuario"));
                }
            };
        }

        @Bean
        CommandLineRunner initUsersDatabase(UsuariosRepository usuariosRepository) {
            return args -> {
                try {
                    Thread.sleep(5000); // Esperar 5 segundos
                    // Crear el rol para el usuario
                    Roles rol = new Roles(1, "Administrador");

                    // Crear un usuario con los datos que ya tienes
                    Usuarios admin = new Usuarios(
                            1, // El ID se genera automáticamente, por lo que puedes poner un valor temporal
                            "Home",
                            "Book",
                            "$2a$10$IfxIObfqEM76kuQhb/12/uT5O8dEjz6HEoNMjm2gxl3Ej4/lV6PGa", // Contraseña encriptada
                            "BookHaven",
                            "BookHaven.com",
                            null, // contrasenaTemporal
                            false, // esContrasenaTemporal
                            rol // Rol
                    );

                    // Guardar el usuario en la base de datos
                    usuariosRepository.save(admin);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
        }

        @PostMapping("/cambiar-contrasenarecuperada")
        public ResponseEntity<String> cambiarContrasena(
                @RequestParam String username,
                @RequestParam String nuevaContrasena) {

            Optional<Usuarios> usuarioOptional = usuariosRepository.findByUsername(username);

            if (!usuarioOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            Usuarios usuario = usuarioOptional.get();

            if (!usuario.isEsContrasenaTemporal()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se puede cambiar la contraseña porque no es temporal.");
            }

            usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
            usuario.setTemporaryPassword(null); // Limpiar la contraseña temporal
            usuario.setEsContrasenaTemporal(false);
            usuariosRepository.save(usuario);

            return ResponseEntity.ok("Contraseña actualizada exitosamente");
        }

    }

    @PutMapping("/usuarios/{id}/actualizar-contrasena")
        @ResponseBody
        public ResponseEntity<String> actualizarContrasena(
                @PathVariable Integer id,
                @RequestBody Map<String, String> contrasenaDatos) {
            try {
                // Obtener los datos del cuerpo de la solicitud
                String contrasenaActual = contrasenaDatos.get("contrasenaActual");
                String nuevaContrasena = contrasenaDatos.get("nuevaContrasena");
                String confirmarContrasena = contrasenaDatos.get("confirmarContrasena");

                // Validar que los datos no sean nulos o vacíos
                if (contrasenaActual == null || nuevaContrasena == null || confirmarContrasena == null ||
                    contrasenaActual.isEmpty() || nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios.");
                }

                // Validar que la nueva contraseña y la confirmación coincidan
                if (!nuevaContrasena.equals(confirmarContrasena)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La nueva contraseña y la confirmación no coinciden.");
                }

                // Validar que la nueva contraseña cumpla los requisitos (ej. longitud mínima)
                if (nuevaContrasena.length() < 8) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La nueva contraseña debe tener al menos 8 caracteres.");
                }

                // Buscar al usuario en la base de datos
                Optional<Usuarios> usuarioOpt = usuariosRepository.findById(id);
                if (usuarioOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
                }

                Usuarios usuario = usuarioOpt.get();

                // Verificar la contraseña actual
                if (!passwordEncoder.matches(contrasenaActual, usuario.getPassword())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La contraseña actual es incorrecta.");
                }

                // Actualizar la contraseña
                usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
                usuariosRepository.save(usuario);

                return ResponseEntity.ok("Contraseña actualizada exitosamente.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la contraseña: " + e.getMessage());
            }
        }
}