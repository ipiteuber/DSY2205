package com.duoc.moderacion.service;

import com.duoc.moderacion.dto.ModeracionDTO;
import com.duoc.moderacion.dto.ModeracionRequest;
import com.duoc.moderacion.dto.ModeracionesPorNorma;
import com.duoc.moderacion.model.TipoContenido;

import java.util.List;

// Contrato del servicio de moderacion
public interface ModeracionService {

    List<ModeracionDTO> listar();

    ModeracionDTO buscarPorId(Long id);

    List<ModeracionDTO> buscarPorModerador(Long idModerador);

    List<ModeracionDTO> buscarPorTipo(TipoContenido tipo);

    List<ModeracionDTO> historialDeContenido(TipoContenido tipo, Long idContenido);

    List<ModeracionesPorNorma> resumenPorNorma();

    ModeracionDTO banear(TipoContenido tipo, Long idContenido, ModeracionRequest datos);

    ModeracionDTO restaurar(TipoContenido tipo, Long idContenido, ModeracionRequest datos);

    ModeracionDTO actualizar(Long id, ModeracionRequest datos);

    void eliminar(Long id);
}
