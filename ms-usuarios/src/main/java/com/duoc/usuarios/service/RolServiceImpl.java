package com.duoc.usuarios.service;

import com.duoc.usuarios.dto.RolDTO;
import com.duoc.usuarios.exception.ConflictoException;
import com.duoc.usuarios.exception.RecursoNoEncontradoException;
import com.duoc.usuarios.mapper.RolMapper;
import com.duoc.usuarios.model.Rol;
import com.duoc.usuarios.repository.RolRepository;
import com.duoc.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementacion del servicio de roles. No deja eliminar un rol que tenga usuarios.
@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolMapper rolMapper;

    @Override
    public List<RolDTO> listar() {
        return rolRepository.findAll().stream().map(rolMapper::aDto).toList();
    }

    @Override
    public RolDTO buscarPorId(Long id) {
        return rolMapper.aDto(obtener(id));
    }

    @Override
    public RolDTO crear(RolDTO dto) {
        if (rolRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new ConflictoException("Ya existe el rol " + dto.getNombre());
        }
        return rolMapper.aDto(rolRepository.save(rolMapper.aEntidad(dto)));
    }

    @Override
    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol rol = obtener(id);
        rolMapper.actualizar(rol, dto);
        return rolMapper.aDto(rolRepository.save(rol));
    }

    @Override
    public void eliminar(Long id) {
        Rol rol = obtener(id);
        if (usuarioRepository.existsByRol_Id(id)) {
            throw new ConflictoException("No se puede eliminar el rol " + rol.getNombre() + " porque tiene usuarios asignados");
        }
        rolRepository.delete(rol);
    }

    private Rol obtener(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un rol con ID " + id));
    }
}
