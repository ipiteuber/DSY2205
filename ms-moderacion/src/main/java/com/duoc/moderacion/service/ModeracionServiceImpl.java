package com.duoc.moderacion.service;

import com.duoc.moderacion.client.ForoClient;
import com.duoc.moderacion.client.UsuarioClient;
import com.duoc.moderacion.dto.ModeracionDTO;
import com.duoc.moderacion.dto.ModeracionRequest;
import com.duoc.moderacion.dto.ModeracionesPorNorma;
import com.duoc.moderacion.dto.UsuarioRemoto;
import com.duoc.moderacion.exception.AccesoDenegadoException;
import com.duoc.moderacion.exception.RecursoNoEncontradoException;
import com.duoc.moderacion.exception.ReglaNegocioException;
import com.duoc.moderacion.mapper.ModeracionMapper;
import com.duoc.moderacion.model.AccionModeracion;
import com.duoc.moderacion.model.Moderacion;
import com.duoc.moderacion.model.Norma;
import com.duoc.moderacion.model.TipoContenido;
import com.duoc.moderacion.repository.ModeracionRepository;
import com.duoc.moderacion.repository.NormaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

// Implementacion del servicio de moderacion, maneja el flujo: Banear -> restaurar. Revisa el rol en ms-usuarios.
@Service
@RequiredArgsConstructor
public class ModeracionServiceImpl implements ModeracionService {

    // ADMIN y MODERADOR pueden banear, pero solo ADMIN puede restaurar
    private static final Set<String> ROLES_BANEAR = Set.of("ADMIN", "MODERADOR");
    private static final Set<String> ROLES_RESTAURAR = Set.of("ADMIN");

    private final ModeracionRepository moderacionRepository;
    private final NormaRepository normaRepository;
    private final ModeracionMapper moderacionMapper;
    private final UsuarioClient usuarioClient;
    private final ForoClient foroClient;

    @Override
    public List<ModeracionDTO> listar() {
        return aDtos(moderacionRepository.findAllByOrderByFechaDesc());
    }

    @Override
    public ModeracionDTO buscarPorId(Long id) {
        return moderacionMapper.aDto(obtener(id));
    }

    @Override
    public List<ModeracionDTO> buscarPorModerador(Long idModerador) {
        return aDtos(moderacionRepository.findByIdModeradorOrderByFechaDesc(idModerador));
    }

    @Override
    public List<ModeracionDTO> buscarPorTipo(TipoContenido tipo) {
        return aDtos(moderacionRepository.findByTipoContenidoOrderByFechaDesc(tipo));
    }

    @Override
    public List<ModeracionDTO> historialDeContenido(TipoContenido tipo, Long idContenido) {
        return aDtos(moderacionRepository.findByTipoContenidoAndIdContenidoOrderByFechaAsc(tipo, idContenido));
    }

    @Override
    public List<ModeracionesPorNorma> resumenPorNorma() {
        return moderacionRepository.contarPorNorma(AccionModeracion.BANEAR);
    }

    @Override
    public ModeracionDTO banear(TipoContenido tipo, Long idContenido, ModeracionRequest datos) {
        validarRol(datos.idModerador(), ROLES_BANEAR, "banear contenido");
        if (datos.idNorma() == null) {
            throw new ReglaNegocioException("Para banear hay que indicar la norma que se incumplió (idNorma)");
        }
        Moderacion moderacion = moderacionMapper.aEntidad(tipo, idContenido, AccionModeracion.BANEAR, datos, obtenerNorma(datos.idNorma()));
        foroClient.cambiarEstado(tipo, idContenido, "BANEADO");
        return moderacionMapper.aDto(moderacionRepository.save(moderacion));
    }

    @Override
    public ModeracionDTO restaurar(TipoContenido tipo, Long idContenido, ModeracionRequest datos) {
        validarRol(datos.idModerador(), ROLES_RESTAURAR, "restaurar contenido");
        Norma norma = datos.idNorma() != null ? obtenerNorma(datos.idNorma()) : null;
        Moderacion moderacion = moderacionMapper.aEntidad(tipo, idContenido, AccionModeracion.RESTAURAR, datos, norma);
        foroClient.cambiarEstado(tipo, idContenido, "ACTIVO");
        return moderacionMapper.aDto(moderacionRepository.save(moderacion));
    }

    @Override
    public ModeracionDTO actualizar(Long id, ModeracionRequest datos) {
        Moderacion moderacion = obtener(id);
        moderacion.setMotivo(datos.motivo());
        if (datos.idNorma() != null) {
            moderacion.setNorma(obtenerNorma(datos.idNorma()));
        }
        return moderacionMapper.aDto(moderacionRepository.save(moderacion));
    }

    @Override
    public void eliminar(Long id) {
        moderacionRepository.delete(obtener(id));
    }

    private void validarRol(Long idUsuario, Set<String> rolesPermitidos, String accion) {
        UsuarioRemoto usuario = usuarioClient.obtenerUsuario(idUsuario);
        if (!Boolean.TRUE.equals(usuario.activo())) {
            throw new AccesoDenegadoException("El usuario " + usuario.nombreUsuario() + " está desactivado");
        }
        if (!rolesPermitidos.contains(usuario.nombreRol())) {
            throw new AccesoDenegadoException("El usuario " + usuario.nombreUsuario() + " tiene rol "
                    + usuario.nombreRol() + " y no tiene permiso para " + accion);
        }
    }

    private Moderacion obtener(Long id) {
        return moderacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una moderación con ID " + id));
    }

    private Norma obtenerNorma(Long id) {
        return normaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una norma con ID " + id));
    }

    private List<ModeracionDTO> aDtos(List<Moderacion> moderaciones) {
        return moderaciones.stream().map(moderacionMapper::aDto).toList();
    }
}
