package com.duoc.moderacion.service;

import com.duoc.moderacion.exception.ConflictoException;
import com.duoc.moderacion.exception.RecursoNoEncontradoException;
import com.duoc.moderacion.model.Norma;
import com.duoc.moderacion.repository.ModeracionRepository;
import com.duoc.moderacion.repository.NormaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio de normas. No deja eliminar una norma que ya se uso en una moderacion.
@Service
@RequiredArgsConstructor
public class NormaService {

    private final NormaRepository normaRepository;
    private final ModeracionRepository moderacionRepository;

    public List<Norma> listar() {
        return normaRepository.findAll();
    }

    public Norma buscarPorId(Long id) {
        return normaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una norma con ID " + id));
    }

    public Norma crear(Norma norma) {
        norma.setId(null);
        return normaRepository.save(norma);
    }

    public Norma actualizar(Long id, Norma datos) {
        Norma norma = buscarPorId(id);
        norma.setTitulo(datos.getTitulo());
        norma.setDescripcion(datos.getDescripcion());
        return normaRepository.save(norma);
    }

    public void eliminar(Long id) {
        Norma norma = buscarPorId(id);
        if (moderacionRepository.existsByNorma_Id(id)) {
            throw new ConflictoException("No se puede eliminar la norma " + id + " porque tiene moderaciones asociadas");
        }
        normaRepository.delete(norma);
    }
}
