package com.duoc.foro.controller;

import com.duoc.foro.dto.ComentarioRequest;
import com.duoc.foro.dto.EstadoRequest;
import com.duoc.foro.dto.TemaDetalle;
import com.duoc.foro.dto.TemaRequest;
import com.duoc.foro.model.Comentario;
import com.duoc.foro.model.Tema;
import com.duoc.foro.service.ComentarioService;
import com.duoc.foro.service.TemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Controlador REST de temas. CRUD, busquedas, comentarios del tema y cambio de estado.
@RestController
@RequestMapping("/api/temas")
@RequiredArgsConstructor
public class TemaController {

    private final TemaService temaService;
    private final ComentarioService comentarioService;

    @GetMapping
    public List<Tema> listar() {
        return temaService.listarActivos();
    }

    @GetMapping("/{id}")
    public TemaDetalle verDetalle(@PathVariable Long id) {
        return temaService.verDetalle(id);
    }

    @GetMapping("/categoria/{idCategoria}")
    public List<Tema> porCategoria(@PathVariable Long idCategoria) {
        return temaService.buscarPorCategoria(idCategoria);
    }

    @GetMapping("/buscar")
    public List<Tema> buscar(@RequestParam String texto) {
        return temaService.buscarPorTexto(texto);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Tema> porUsuario(@PathVariable Long idUsuario) {
        return temaService.buscarPorUsuario(idUsuario);
    }

    @GetMapping("/populares")
    public List<Tema> populares() {
        return temaService.populares();
    }

    @GetMapping("/baneados")
    public List<Tema> baneados() {
        return temaService.listarBaneados();
    }

    @PostMapping
    public ResponseEntity<Tema> publicar(@Valid @RequestBody TemaRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaService.publicar(datos));
    }

    @PutMapping("/{id}")
    public Tema actualizar(@PathVariable Long id, @Valid @RequestBody TemaRequest datos) {
        return temaService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Long id) {
        temaService.eliminar(id);
        return Map.of("mensaje", "Tema con ID " + id + " y sus comentarios eliminados correctamente");
    }

    // Lo usa ms-moderacion para banear o restaurar
    @PatchMapping("/{id}/estado")
    public Tema cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest datos) {
        return temaService.cambiarEstado(id, datos.estado());
    }

    @GetMapping("/{id}/comentarios")
    public List<Comentario> comentarios(@PathVariable Long id) {
        return comentarioService.listarPorTema(id);
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<Comentario> comentar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.comentar(id, datos));
    }
}
