package ${package}.service;

import ${package}.dto.${entidad}DTO;

import java.util.List;

// Contrato del servicio de ${recurso}
public interface ${entidad}Service {

    List<${entidad}DTO> listar();

    ${entidad}DTO buscarPorId(Long id);

    ${entidad}DTO crear(${entidad}DTO dto);

    ${entidad}DTO actualizar(Long id, ${entidad}DTO dto);

    void eliminar(Long id);
}
