package com.duoc.usuarios.service;

import com.duoc.usuarios.exception.ConflictoException;
import com.duoc.usuarios.exception.RecursoNoEncontradoException;
import com.duoc.usuarios.model.Rol;
import com.duoc.usuarios.repository.RolRepository;
import com.duoc.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio de roles. No deja eliminar un rol que tenga usuarios.
@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un rol con ID " + id));
    }

    public Rol crear(Rol rol) {
        if (rolRepository.existsByNombreIgnoreCase(rol.getNombre())) {
            throw new ConflictoException("Ya existe el rol " + rol.getNombre());
        }
        rol.setId(null);
        rol.setNombre(rol.getNombre().toUpperCase());
        return rolRepository.save(rol);
    }

    public Rol actualizar(Long id, Rol datos) {
        Rol rol = buscarPorId(id);
        rol.setNombre(datos.getNombre().toUpperCase());
        rol.setDescripcion(datos.getDescripcion());
        return rolRepository.save(rol);
    }

    public void eliminar(Long id) {
        Rol rol = buscarPorId(id);
        if (usuarioRepository.existsByRol_Id(id)) {
            throw new ConflictoException("No se puede eliminar el rol " + rol.getNombre() + " porque tiene usuarios asignados");
        }
        rolRepository.delete(rol);
    }
}
