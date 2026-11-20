#set( $var = "${entidad.substring(0,1).toLowerCase()}${entidad.substring(1)}" )
package ${package}.mapper;

import ${package}.dto.${entidad}DTO;
import ${package}.model.${entidad};
import org.springframework.stereotype.Component;

// Convierte entre ${entidad} y ${entidad}DTO usando el builder de cada clase
@Component
public class ${entidad}Mapper {

    public ${entidad}DTO aDto(${entidad} ${var}) {
        return ${entidad}DTO.builder()
                .id(${var}.getId())
                .nombre(${var}.getNombre())
                .build();
    }

    public ${entidad} aEntidad(${entidad}DTO dto) {
        return ${entidad}.builder()
                .nombre(dto.getNombre())
                .build();
    }

    // Copia los datos del DTO sobre una entidad que ya existe (para el PUT)
    public void actualizar(${entidad} ${var}, ${entidad}DTO dto) {
        ${var}.setNombre(dto.getNombre());
    }
}
