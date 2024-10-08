package com.example.demo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libros, Integer> {
    Optional<Libros> findByTitulo(String titulo);
}
