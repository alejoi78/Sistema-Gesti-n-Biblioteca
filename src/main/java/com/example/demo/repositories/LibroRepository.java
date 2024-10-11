package com.example.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Libros;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libros, Integer> {
    Optional<Libros> findByTitulo(String titulo);
}
