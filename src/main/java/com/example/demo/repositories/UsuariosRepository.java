package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Usuarios;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByUsername(String Username);

    Optional<Usuarios> findByCorreoelectronico(String correoelectronico);

    boolean existsByUsername(String Username);

    boolean existsByCorreoelectronico(String correoelectronico);
    

}
