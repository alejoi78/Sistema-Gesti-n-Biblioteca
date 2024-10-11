package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Roles;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Roles, Integer> {
    Roles findByTipodeRol(String tipodeRol);
    Optional<Roles> findById(int idRol);  // Método personalizado
}
