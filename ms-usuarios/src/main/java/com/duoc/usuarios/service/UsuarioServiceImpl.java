package com.duoc.usuarios.service;

import com.duoc.usuarios.dto.LoginRequest;
import com.duoc.usuarios.dto.LoginResponse;
import com.duoc.usuarios.dto.UsuarioDTO;
import com.duoc.usuarios.dto.UsuarioRequest;
import com.duoc.usuarios.dto.UsuariosPorRol;
import com.duoc.usuarios.exception.AccesoDenegadoException;
import com.duoc.usuarios.exception.ConflictoException;
import com.duoc.usuarios.exception.CredencialesInvalidasException;
import com.duoc.usuarios.exception.RecursoNoEncontradoException;
import com.duoc.usuarios.exception.ReglaNegocioException;
import com.duoc.usuarios.mapper.UsuarioMapper;
import com.duoc.usuarios.model.Rol;
import com.duoc.usuarios.model.Usuario;
import com.duoc.usuarios.repository.RolRepository;
import com.duoc.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementacion del servicio de usuarios, maneja el CRUD, los filtros y el login con BCrypt
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> listar() {
        return aDtos(usuarioRepository.findAll());
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        return usuarioMapper.aDto(obtener(id));
    }

    @Override
    public List<UsuarioDTO> buscarPorRol(String nombreRol) {
        return aDtos(usuarioRepository.findByRol_NombreIgnoreCase(nombreRol));
    }

    @Override
    public List<UsuarioDTO> buscarPorEstado(Boolean activo) {
        return aDtos(usuarioRepository.findByActivo(activo));
    }

    @Override
    public List<UsuarioDTO> buscarPorTexto(String texto) {
        return aDtos(usuarioRepository.buscarPorTexto(texto));
    }

    @Override
    public List<UsuariosPorRol> resumenPorRol() {
        return usuarioRepository.contarPorRol();
    }

    @Override
    public UsuarioDTO crear(UsuarioRequest datos) {
        if (datos.password() == null || datos.password().isBlank()) {
            throw new ReglaNegocioException("La contraseña es obligatoria al crear un usuario");
        }
        if (usuarioRepository.existsByNombreUsuarioIgnoreCase(datos.nombreUsuario())) {
            throw new ConflictoException("El nombre de usuario " + datos.nombreUsuario() + " ya está en uso");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(datos.email())) {
            throw new ConflictoException("El email " + datos.email() + " ya está registrado");
        }
        Usuario usuario = usuarioMapper.aEntidad(datos, obtenerRol(datos.idRol()), passwordEncoder.encode(datos.password()));
        return usuarioMapper.aDto(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioRequest datos) {
        Usuario usuario = obtener(id);
        if (!usuario.getNombreUsuario().equalsIgnoreCase(datos.nombreUsuario())
                && usuarioRepository.existsByNombreUsuarioIgnoreCase(datos.nombreUsuario())) {
            throw new ConflictoException("El nombre de usuario " + datos.nombreUsuario() + " ya está en uso");
        }
        if (!usuario.getEmail().equalsIgnoreCase(datos.email())
                && usuarioRepository.existsByEmailIgnoreCase(datos.email())) {
            throw new ConflictoException("El email " + datos.email() + " ya está registrado");
        }
        usuarioMapper.actualizar(usuario, datos, obtenerRol(datos.idRol()));
        // Si la password viene vacia se mantiene la actual
        if (datos.password() != null && !datos.password().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(datos.password()));
        }
        return usuarioMapper.aDto(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.delete(obtener(id));
    }

    @Override
    public LoginResponse login(LoginRequest datos) {
        Usuario usuario = usuarioRepository
                .findByNombreUsuarioIgnoreCaseOrEmailIgnoreCase(datos.usuario(), datos.usuario())
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos"));
        if (!passwordEncoder.matches(datos.password(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }
        if (!usuario.getActivo()) {
            throw new AccesoDenegadoException("La cuenta de " + usuario.getNombreUsuario() + " está desactivada");
        }
        return new LoginResponse(usuario.getId(), usuario.getNombreUsuario(), usuario.getNombreCompleto(),
                usuario.getRol().getNombre(), "Inicio de sesión exitoso. Bienvenido/a, " + usuario.getNombreCompleto());
    }

    private Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con ID " + id));
    }

    private Rol obtenerRol(Long idRol) {
        return rolRepository.findById(idRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un rol con ID " + idRol));
    }

    private List<UsuarioDTO> aDtos(List<Usuario> usuarios) {
        return usuarios.stream().map(usuarioMapper::aDto).toList();
    }
}
