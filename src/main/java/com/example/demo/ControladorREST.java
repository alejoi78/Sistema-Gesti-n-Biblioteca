package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;

@Controller
public class ControladorREST {

    @Autowired
    private UsuariosRepository usuariosRepository;

    // Crear un codificador BCrypt
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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


    // Método para obtener todos los usuarios (HTTP GET)
    @GetMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<List<Usuarios>> getUsuarios() {
        List<Usuarios> usuarios = usuariosRepository.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Método para agregar un nuevo usuario (HTTP POST)
    @PostMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<Usuarios> agregarUsuario(@RequestBody Usuarios nuevoUsuario) {
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword())); // Encriptar la contraseña
        Usuarios usuarioGuardado = usuariosRepository.save(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    // Método para eliminar un usuario por ID (HTTP DELETE)
    @DeleteMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {
        if (usuariosRepository.existsById(id)) {
            usuariosRepository.deleteById(id);
            return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // Método para actualizar un usuario (HTTP PUT)
    @PutMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<String> editarUsuario(@PathVariable Integer id, @RequestBody Usuarios usuarioActualizado) {
        Optional<Usuarios> usuarioExistenteOpt = usuariosRepository.findById(id);
        if (usuarioExistenteOpt.isPresent()) {
            Usuarios usuarioExistente = usuarioExistenteOpt.get();
            // Actualizar solo si los valores son proporcionados
            if (usuarioActualizado.getNombre() != null && !usuarioActualizado.getNombre().isEmpty()) {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado.getApellido() != null && !usuarioActualizado.getApellido().isEmpty()) {
                usuarioExistente.setApellido(usuarioActualizado.getApellido());
            }
            // No es recomendable cambiar el ID ni la contraseña aquí
            usuariosRepository.save(usuarioExistente);
            return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
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
            
            // Verificar la contraseña usando BCrypt
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                
                Map<String, Object> claims = new HashMap<>();
                claims.put("role", "USER"); // Añade roles o cualquier otra información
        
                // Fecha de expiración del token (ej. 10 minutos)
                long expirationTime = 1000 * 60 * 10;
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

}
