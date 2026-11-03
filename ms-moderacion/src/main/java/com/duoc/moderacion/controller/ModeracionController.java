package com.duoc.moderacion.controller;

import com.duoc.moderacion.dto.ModeracionRequest;
import com.duoc.moderacion.dto.ModeracionesPorNorma;
import com.duoc.moderacion.model.Moderacion;
import com.duoc.moderacion.model.TipoContenido;
import com.duoc.moderacion.service.ModeracionService;
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

// Controlador REST de moderacion. Banear, restaurar, historial y resumen por norma.
@RestController
@RequestMapping("/api/moderacion")
@RequiredArgsConstructor
public class ModeracionController {

    private final ModeracionService moderacionService;

    @GetMapping
    public List<Moderacion> listar() {
        return moderacionService.listar();
    }

    @GetMapping("/{id}")
    public Moderacion obtener(@PathVariable Long id) {
        return moderacionService.buscarPorId(id);
    }

    @GetMapping("/moderador/{idModerador}")
    public List<Moderacion> porModerador(@PathVariable Long idModerador) {
        return moderacionService.buscarPorModerador(idModerador);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Moderacion> porTipo(@PathVariable TipoContenido tipo) {
        return moderacionService.buscarPorTipo(tipo);
    }

    @GetMapping("/historial/{tipo}/{idContenido}")
    public List<Moderacion> historial(@PathVariable TipoContenido tipo, @PathVariable Long idContenido) {
        return moderacionService.historialDeContenido(tipo, idContenido);
    }

    @GetMapping("/resumen-normas")
    public List<ModeracionesPorNorma> resumenPorNorma() {
        return moderacionService.resumenPorNorma();
    }

    @PostMapping("/temas/{id}/banear")
    public ResponseEntity<Moderacion> banearTema(@PathVariable Long id, @Valid @RequestBody ModeracionRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moderacionService.banear(TipoContenido.TEMA, id, datos));
    }

    @PostMapping("/temas/{id}/restaurar")
    public ResponseEntity<Moderacion> restaurarTema(@PathVariable Long id, @Valid @RequestBody ModeracionRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moderacionService.restaurar(TipoContenido.TEMA, id, datos));
    }

    @PostMapping("/comentarios/{id}/banear")
    public ResponseEntity<Moderacion> banearComentario(@PathVariable Long id, @Valid @RequestBody ModeracionRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moderacionService.banear(TipoContenido.COMENTARIO, id, datos));
    }

    @PostMapping("/comentarios/{id}/restaurar")
    public ResponseEntity<Moderacion> restaurarComentario(@PathVariable Long id, @Valid @RequestBody ModeracionRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moderacionService.restaurar(TipoContenido.COMENTARIO, id, datos));
    }

    @PutMapping("/{id}")
    public Moderacion actualizar(@PathVariable Long id, @Valid @RequestBody ModeracionRequest datos) {
        return moderacionService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Long id) {
        moderacionService.eliminar(id);
        return Map.of("mensaje", "Registro de moderación con ID " + id + " eliminado correctamente");
    }
}
