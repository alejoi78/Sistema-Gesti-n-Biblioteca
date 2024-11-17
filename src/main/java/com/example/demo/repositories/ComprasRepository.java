package com.example.demo.repositories;

import com.example.demo.models.Compras;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComprasRepository extends JpaRepository<Compras, Integer> {

    // Buscar compras por el nombre de usuario
    List<Compras> findByUsername(String username);

    // Buscar compras por el título del libro
    List<Compras> findByLibroTitulo(String titulo);
}
