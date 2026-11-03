package com.duoc.usuarios.controller;

import com.duoc.usuarios.dto.UsuarioRequest;
import com.duoc.usuarios.dto.UsuariosPorRol;
import com.duoc.usuarios.model.Usuario;
import com.duoc.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Controlador REST de usuarios. CRUD mas filtros por rol, estado, texto y resumen por rol.
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/rol/{nombreRol}")
    public List<Usuario> porRol(@PathVariable String nombreRol) {
        return usuarioService.buscarPorRol(nombreRol);
    }

    @GetMapping("/estado/{activo}")
    public List<Usuario> porEstado(@PathVariable Boolean activo) {
        return usuarioService.buscarPorEstado(activo);
    }

    @GetMapping("/buscar")
    public List<Usuario> buscar(@RequestParam String texto) {
        return usuarioService.buscarPorTexto(texto);
    }

    @GetMapping("/resumen-roles")
    public List<UsuariosPorRol> resumenPorRol() {
        return usuarioService.resumenPorRol();
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(datos));
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest datos) {
        return usuarioService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return Map.of("mensaje", "Usuario con ID " + id + " eliminado correctamente");
    }
}
