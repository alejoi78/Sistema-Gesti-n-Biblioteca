package com.example.demo.controllers;

import com.example.demo.models.Compras;
import com.example.demo.models.Libros;
import com.example.demo.models.Usuarios;
import com.example.demo.repositories.ComprasRepository;
import com.example.demo.repositories.LibroRepository;
import com.example.demo.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.example.demo.models.CompraRequest; // Asegúrate de que la importación sea correcta

@RestController
@RequestMapping("/compras")
public class ComprasController {

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private LibroRepository libroRepository;

    // Registrar una compra
    @PostMapping("/comprar")
    @ResponseBody
    public ResponseEntity<Compras> registrarCompra(@RequestBody CompraRequest compraRequest) {
        // Buscar el usuario y el libro
        Usuarios usuario = usuariosRepository.findByUsername(compraRequest.getUsername())
                .orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Libros libro = libroRepository.findByTitulo(compraRequest.getTituloLibro())
                .orElse(null);
        if (libro == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Crear y guardar la compra
        Compras compra = new Compras();
        compra.setUsuario(usuario);
        compra.setLibro(libro);
        compra.setUsername(usuario.getUsername());
        compra.setCorreoElectronico(usuario.getCorreoelectronico());
        compra.setTituloLibro(libro.getTitulo());
        compra.setFechaCompra(new Date());
        compra.setPrecio(libro.getPrecio());

        comprasRepository.save(compra);
        return new ResponseEntity<>(compra, HttpStatus.CREATED);
    }

    // Listar todas las compras
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Compras>> listarCompras() {
        List<Compras> compras = comprasRepository.findAll();
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    // Obtener compras por usuario
    @GetMapping("/usuario/{username}")
    @ResponseBody
    public ResponseEntity<List<Compras>> obtenerComprasPorUsuario(@PathVariable String username) {
        List<Compras> compras = comprasRepository.findByUsername(username);
        if (compras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    // Obtener compras por libro
    @GetMapping("/libro/{titulo}")
    @ResponseBody
    public ResponseEntity<List<Compras>> obtenerComprasPorLibro(@PathVariable String titulo) {
        List<Compras> compras = comprasRepository.findByLibroTitulo(titulo);
        if (compras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }
}
