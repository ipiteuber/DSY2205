package com.duoc.foro.service;

import com.duoc.foro.dto.CategoriaResumen;
import com.duoc.foro.exception.ConflictoException;
import com.duoc.foro.exception.RecursoNoEncontradoException;
import com.duoc.foro.model.Categoria;
import com.duoc.foro.model.EstadoContenido;
import com.duoc.foro.repository.CategoriaRepository;
import com.duoc.foro.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio de categorias. No deja eliminar una categoria que tenga temas.
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final TemaRepository temaRepository;

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una categoría con ID " + id));
    }

    public List<CategoriaResumen> resumen() {
        return categoriaRepository.resumenTemas(EstadoContenido.ACTIVO);
    }

    public Categoria crear(Categoria categoria) {
        if (categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre())) {
            throw new ConflictoException("Ya existe la categoría " + categoria.getNombre());
        }
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria datos) {
        Categoria categoria = buscarPorId(id);
        categoria.setNombre(datos.getNombre());
        categoria.setDescripcion(datos.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        if (temaRepository.existsByCategoria_Id(id)) {
            throw new ConflictoException("No se puede eliminar " + categoria.getNombre() + " porque tiene temas publicados");
        }
        categoriaRepository.delete(categoria);
    }
}
