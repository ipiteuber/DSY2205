package com.duoc.usuarios.service;

import com.duoc.usuarios.dto.LoginRequest;
import com.duoc.usuarios.dto.LoginResponse;
import com.duoc.usuarios.dto.UsuarioRequest;
import com.duoc.usuarios.dto.UsuariosPorRol;
import com.duoc.usuarios.exception.AccesoDenegadoException;
import com.duoc.usuarios.exception.ConflictoException;
import com.duoc.usuarios.exception.CredencialesInvalidasException;
import com.duoc.usuarios.exception.RecursoNoEncontradoException;
import com.duoc.usuarios.exception.ReglaNegocioException;
import com.duoc.usuarios.model.Usuario;
import com.duoc.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio de usuarios, maneja el CRUD, los filtros y el login con BCrypt
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con ID " + id));
    }

    public List<Usuario> buscarPorRol(String nombreRol) {
        return usuarioRepository.findByRol_NombreIgnoreCase(nombreRol);
    }

    public List<Usuario> buscarPorEstado(Boolean activo) {
        return usuarioRepository.findByActivo(activo);
    }

    public List<Usuario> buscarPorTexto(String texto) {
        return usuarioRepository.buscarPorTexto(texto);
    }

    public List<UsuariosPorRol> resumenPorRol() {
        return usuarioRepository.contarPorRol();
    }

    public Usuario crear(UsuarioRequest datos) {
        if (datos.password() == null || datos.password().isBlank()) {
            throw new ReglaNegocioException("La contraseña es obligatoria al crear un usuario");
        }
        if (usuarioRepository.existsByNombreUsuarioIgnoreCase(datos.nombreUsuario())) {
            throw new ConflictoException("El nombre de usuario " + datos.nombreUsuario() + " ya está en uso");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(datos.email())) {
            throw new ConflictoException("El email " + datos.email() + " ya está registrado");
        }
        Usuario usuario = new Usuario();
        copiarDatos(usuario, datos);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, UsuarioRequest datos) {
        Usuario usuario = buscarPorId(id);
        if (!usuario.getNombreUsuario().equalsIgnoreCase(datos.nombreUsuario())
                && usuarioRepository.existsByNombreUsuarioIgnoreCase(datos.nombreUsuario())) {
            throw new ConflictoException("El nombre de usuario " + datos.nombreUsuario() + " ya está en uso");
        }
        if (!usuario.getEmail().equalsIgnoreCase(datos.email())
                && usuarioRepository.existsByEmailIgnoreCase(datos.email())) {
            throw new ConflictoException("El email " + datos.email() + " ya está registrado");
        }
        copiarDatos(usuario, datos);
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.delete(buscarPorId(id));
    }

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

    private void copiarDatos(Usuario usuario, UsuarioRequest datos) {
        usuario.setNombreUsuario(datos.nombreUsuario());
        usuario.setEmail(datos.email());
        usuario.setNombreCompleto(datos.nombreCompleto());
        usuario.setRol(rolService.buscarPorId(datos.idRol()));
        if (datos.activo() != null) {
            usuario.setActivo(datos.activo());
        }
        if (datos.password() != null && !datos.password().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(datos.password()));
        }
    }
}
