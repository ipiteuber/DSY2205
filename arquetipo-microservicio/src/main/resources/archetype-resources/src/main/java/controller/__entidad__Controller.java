#set( $var = "${entidad.substring(0,1).toLowerCase()}${entidad.substring(1)}" )
package ${package}.controller;

import ${package}.dto.${entidad}DTO;
import ${package}.service.${entidad}Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Controlador REST de ${recurso}, CRUD en /api/${recurso}
@RestController
@RequestMapping("/api/${recurso}")
@RequiredArgsConstructor
public class ${entidad}Controller {

    private final ${entidad}Service ${var}Service;

    @GetMapping
    public List<${entidad}DTO> listar() {
        return ${var}Service.listar();
    }

    @GetMapping("/{id}")
    public ${entidad}DTO obtener(@PathVariable Long id) {
        return ${var}Service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<${entidad}DTO> crear(@Valid @RequestBody ${entidad}DTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(${var}Service.crear(dto));
    }

    @PutMapping("/{id}")
    public ${entidad}DTO actualizar(@PathVariable Long id, @Valid @RequestBody ${entidad}DTO dto) {
        return ${var}Service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable Long id) {
        ${var}Service.eliminar(id);
        return Map.of("mensaje", "Registro con ID " + id + " eliminado correctamente");
    }
}
