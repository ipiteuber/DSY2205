package com.duoc.moderacion.controller;

import com.duoc.moderacion.dto.NormaDTO;
import com.duoc.moderacion.service.NormaService;
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

// Controlador REST de normas, CRUD en /api/normas
@RestController
@RequestMapping("/api/normas")
@RequiredArgsConstructor
public class NormaController {

    private final NormaService normaService;

    @GetMapping
    public List<NormaDTO> listar() {
        return normaService.listar();
    }

    @GetMapping("/{id}")
    public NormaDTO obtener(@PathVariable Long id) {
        return normaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<NormaDTO> crear(@Valid @RequestBody NormaDTO norma) {
        return ResponseEntity.status(HttpStatus.CREATED).body(normaService.crear(norma));
    }

    @PutMapping("/{id}")
    public NormaDTO actualizar(@PathVariable Long id, @Valid @RequestBody NormaDTO norma) {
        return normaService.actualizar(id, norma);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Long id) {
        normaService.eliminar(id);
        return Map.of("mensaje", "Norma con ID " + id + " eliminada correctamente");
    }
}
