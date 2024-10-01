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

    @PutMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<String> editarUsuario(@PathVariable Integer id, @RequestBody Usuarios usuarioActualizado) {
        Optional<Usuarios> usuarioExistenteOpt = usuariosRepository.findById(id);

        if (usuarioExistenteOpt.isPresent()) {
            Usuarios usuarioExistente = usuarioExistenteOpt.get();

            // Actualiza solo si los valores son proporcionados y no son nulos
            if (usuarioActualizado.getNombre() != null && !usuarioActualizado.getNombre().isEmpty()) {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado.getApellido() != null && !usuarioActualizado.getApellido().isEmpty()) {
                usuarioExistente.setApellido(usuarioActualizado.getApellido());
            }

            // Nota: No es recomendable cambiar el ID, por lo que omito la parte de
            // actualización del ID

            usuariosRepository.save(usuarioExistente); // Guarda los cambios

            return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

}
