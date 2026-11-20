package ${package}.repository;

import ${package}.model.${entidad};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio JPA de ${entidad}. Hereda findAll, findById, save, deleteById, existsById.
@Repository
public interface ${entidad}Repository extends JpaRepository<${entidad}, Long> {
}
