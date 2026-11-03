package com.duoc.usuarios.repository;

import com.duoc.usuarios.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio JPA de Rol
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
}
