package com.duoc.foro.service;

import com.duoc.foro.dto.CategoriaDTO;
import com.duoc.foro.dto.CategoriaResumen;
import com.duoc.foro.exception.ConflictoException;
import com.duoc.foro.exception.RecursoNoEncontradoException;
import com.duoc.foro.mapper.CategoriaMapper;
import com.duoc.foro.model.Categoria;
import com.duoc.foro.model.EstadoContenido;
import com.duoc.foro.repository.CategoriaRepository;
import com.duoc.foro.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementacion del servicio de categorias. No deja eliminar una categoria que tenga temas.
@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final TemaRepository temaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll().stream().map(categoriaMapper::aDto).toList();
    }

    @Override
    public CategoriaDTO buscarPorId(Long id) {
        return categoriaMapper.aDto(obtener(id));
    }

    @Override
    public List<CategoriaResumen> resumen() {
        return categoriaRepository.resumenTemas(EstadoContenido.ACTIVO);
    }

    @Override
    public CategoriaDTO crear(CategoriaDTO dto) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new ConflictoException("Ya existe la categoría " + dto.getNombre());
        }
        return categoriaMapper.aDto(categoriaRepository.save(categoriaMapper.aEntidad(dto)));
    }

    @Override
    public CategoriaDTO actualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = obtener(id);
        categoriaMapper.actualizar(categoria, dto);
        return categoriaMapper.aDto(categoriaRepository.save(categoria));
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = obtener(id);
        if (temaRepository.existsByCategoria_Id(id)) {
            throw new ConflictoException("No se puede eliminar " + categoria.getNombre() + " porque tiene temas publicados");
        }
        categoriaRepository.delete(categoria);
    }

    private Categoria obtener(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una categoría con ID " + id));
    }
}
