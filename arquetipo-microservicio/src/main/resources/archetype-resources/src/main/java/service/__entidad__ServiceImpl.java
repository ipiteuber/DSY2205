#set( $var = "${entidad.substring(0,1).toLowerCase()}${entidad.substring(1)}" )
package ${package}.service;

import ${package}.dto.${entidad}DTO;
import ${package}.exception.RecursoNoEncontradoException;
import ${package}.mapper.${entidad}Mapper;
import ${package}.model.${entidad};
import ${package}.repository.${entidad}Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Implementacion del servicio de ${recurso}, usa ${entidad}Repository y ${entidad}Mapper
@Service
@RequiredArgsConstructor
public class ${entidad}ServiceImpl implements ${entidad}Service {

    private final ${entidad}Repository ${var}Repository;
    private final ${entidad}Mapper ${var}Mapper;

    @Override
    public List<${entidad}DTO> listar() {
        return ${var}Repository.findAll().stream()
                .map(${var}Mapper::aDto)
                .toList();
    }

    @Override
    public ${entidad}DTO buscarPorId(Long id) {
        return ${var}Mapper.aDto(obtener(id));
    }

    @Override
    public ${entidad}DTO crear(${entidad}DTO dto) {
        ${entidad} ${var} = ${var}Mapper.aEntidad(dto);
        return ${var}Mapper.aDto(${var}Repository.save(${var}));
    }

    @Override
    @Transactional
    public ${entidad}DTO actualizar(Long id, ${entidad}DTO dto) {
        ${entidad} ${var} = obtener(id);
        ${var}Mapper.actualizar(${var}, dto);
        return ${var}Mapper.aDto(${var}Repository.save(${var}));
    }

    @Override
    public void eliminar(Long id) {
        ${var}Repository.delete(obtener(id));
    }

    // Busca la entidad, lanza 404 si no existe
    private ${entidad} obtener(Long id) {
        return ${var}Repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un registro con ID " + id));
    }
}
