package ${package}.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO de ${entidad}. Es lo que entra y sale por la API, la entidad JPA no se expone.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ${entidad}DTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "No puede superar los 100 caracteres")
    private String nombre;
}
