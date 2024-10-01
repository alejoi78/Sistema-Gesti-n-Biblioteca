package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ControladorREST {

    @Autowired
    private UsuariosRepository usuariosRepository;

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

    // Método para agregar un nuevo usuario (HTTP POST)
    @PostMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<Usuarios> agregarUsuario(@RequestBody Usuarios nuevoUsuario) {
        Usuarios usuarioGuardado = usuariosRepository.save(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    // Método para obtener todos los usuarios (HTTP GET)
    @GetMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<List<Usuarios>> getUsuarios() {
        List<Usuarios> usuarios = usuariosRepository.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
}
