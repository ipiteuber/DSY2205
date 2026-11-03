package com.duoc.usuarios.repository;

import com.duoc.usuarios.dto.UsuariosPorRol;
import com.duoc.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio JPA de Usuario, con la busqueda por texto y el resumen por rol
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuarioIgnoreCaseOrEmailIgnoreCase(String nombreUsuario, String email);

    List<Usuario> findByRol_NombreIgnoreCase(String nombreRol);

    List<Usuario> findByActivo(Boolean activo);

    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByRol_Id(Long idRol);

    @Query("""
            SELECT u FROM Usuario u
            WHERE LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :texto, '%'))
            ORDER BY u.nombreUsuario
            """)
    List<Usuario> buscarPorTexto(@Param("texto") String texto);

    @Query("""
            SELECT new com.duoc.usuarios.dto.UsuariosPorRol(r.nombre, COUNT(u))
            FROM Rol r LEFT JOIN Usuario u ON u.rol = r
            GROUP BY r.nombre
            ORDER BY r.nombre
            """)
    List<UsuariosPorRol> contarPorRol();
}
