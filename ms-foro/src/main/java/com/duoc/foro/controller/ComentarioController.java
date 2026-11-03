package com.duoc.foro.controller;

import com.duoc.foro.dto.ComentarioRequest;
import com.duoc.foro.dto.EstadoRequest;
import com.duoc.foro.model.Comentario;
import com.duoc.foro.service.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Controlador REST de comentarios. Los comentarios se crean desde /api/temas/{id}/comentarios.
@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> listar() {
        return comentarioService.listar();
    }

    @GetMapping("/{id}")
    public Comentario obtener(@PathVariable Long id) {
        return comentarioService.buscarPorId(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Comentario> porUsuario(@PathVariable Long idUsuario) {
        return comentarioService.buscarPorUsuario(idUsuario);
    }

    @GetMapping("/baneados")
    public List<Comentario> baneados() {
        return comentarioService.listarBaneados();
    }

    @PutMapping("/{id}")
    public Comentario actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest datos) {
        return comentarioService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Long id) {
        comentarioService.eliminar(id);
        return Map.of("mensaje", "Comentario con ID " + id + " eliminado correctamente");
    }

    @PatchMapping("/{id}/estado")
    public Comentario cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest datos) {
        return comentarioService.cambiarEstado(id, datos.estado());
    }
}
