package com.duoc.usuarios.controller;

import com.duoc.usuarios.dto.RolDTO;
import com.duoc.usuarios.service.RolService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Controlador REST de roles, CRUD en /api/roles
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    public List<RolDTO> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public RolDTO obtener(@PathVariable Long id) {
        return rolService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<RolDTO> crear(@Valid @RequestBody RolDTO rol) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(rol));
    }

    @PutMapping("/{id}")
    public RolDTO actualizar(@PathVariable Long id, @Valid @RequestBody RolDTO rol) {
        return rolService.actualizar(id, rol);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return Map.of("mensaje", "Rol con ID " + id + " eliminado correctamente");
    }
}
