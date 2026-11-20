package com.duoc.moderacion.service;

import com.duoc.moderacion.dto.NormaDTO;
import com.duoc.moderacion.exception.ConflictoException;
import com.duoc.moderacion.exception.RecursoNoEncontradoException;
import com.duoc.moderacion.mapper.NormaMapper;
import com.duoc.moderacion.model.Norma;
import com.duoc.moderacion.repository.ModeracionRepository;
import com.duoc.moderacion.repository.NormaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementacion del servicio de normas. No deja eliminar una norma que ya se uso en una moderacion.
@Service
@RequiredArgsConstructor
public class NormaServiceImpl implements NormaService {

    private final NormaRepository normaRepository;
    private final ModeracionRepository moderacionRepository;
    private final NormaMapper normaMapper;

    @Override
    public List<NormaDTO> listar() {
        return normaRepository.findAll().stream().map(normaMapper::aDto).toList();
    }

    @Override
    public NormaDTO buscarPorId(Long id) {
        return normaMapper.aDto(obtener(id));
    }

    @Override
    public NormaDTO crear(NormaDTO dto) {
        return normaMapper.aDto(normaRepository.save(normaMapper.aEntidad(dto)));
    }

    @Override
    public NormaDTO actualizar(Long id, NormaDTO dto) {
        Norma norma = obtener(id);
        normaMapper.actualizar(norma, dto);
        return normaMapper.aDto(normaRepository.save(norma));
    }

    @Override
    public void eliminar(Long id) {
        Norma norma = obtener(id);
        if (moderacionRepository.existsByNorma_Id(id)) {
            throw new ConflictoException("No se puede eliminar la norma " + id + " porque tiene moderaciones asociadas");
        }
        normaRepository.delete(norma);
    }

    private Norma obtener(Long id) {
        return normaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una norma con ID " + id));
    }
}
